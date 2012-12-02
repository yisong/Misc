// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>

#include <glib.h>
#include <gdk/gdk.h>
#include <gtk/gtk.h>

#include "eventManager.h"
#include "launchpadData.h"
#include "lockDef.h"

#define NWORKERS 5 

EXPOSETYPE(onData_t)
EXPOSETYPE(fireData_t)

INSTANTIATETYPE(onData_t, launchpad_data_t *btn_info;
                          int index;
                          GtkWidget *button;)

INSTANTIATETYPE(fireData_t, LOCK_t *lock;
                            int *runPost;
                            int *num_active;
                            int *num_remaining;
                            GtkWidget *footage;)

void onDataRelease(eventHandler_closure_t onData)
{
  onData_t_release((onData_t) onData);
}

fireData_t fireData_create(int workers, int num_btns, GtkWidget *footage)
{
  fireData_t fireData = fireData_t_acquire();
  if (fireData == NULL)
    return NULL;
  LOCK_t *l = (LOCK_t *) malloc(sizeof(LOCK_t));
  LOCK_INIT(l);
  if (l == NULL)
    return NULL;
  int *num_active = (int *) calloc(num_btns, sizeof(int));
  if (num_active == NULL)
    return NULL;
  int *num_remaining = (int *) malloc(sizeof(int));
  if (num_remaining == NULL)
    return NULL;
  int *runPost = (int *) calloc(1, sizeof(int));
  if (runPost == NULL) 
    return NULL;
  *num_remaining = workers;
  fireData->lock = l;
  fireData->num_active = num_active;
  fireData->num_remaining = num_remaining;
  fireData->runPost = runPost;
  fireData->footage = footage;
  return fireData;
}

void fireDataDestructor(fireData_t fireData)
{
  LOCK_DESTROY(fireData->lock);
  free(fireData->lock);
  free(fireData->runPost);
  free(fireData->num_active);
  free(fireData->num_remaining);
}

CREATEMANAGER(onData_t, NULL)
CREATEMANAGER(fireData_t, fireDataDestructor)

static void setGuiCount(GtkWidget *footage, GtkWidget *label, 
                        int num_remaining, int num_active)
{
  gdk_threads_enter();
  char buf[20];
  sprintf(buf, "%d launches remaining", num_remaining);
  gtk_label_set_text(GTK_LABEL(footage), buf);
  sprintf(buf, "%d active", num_active);
  gtk_label_set_text(GTK_LABEL(label), buf); 
  gdk_threads_leave();
}

static void handler(eventHandler_closure_t onData, 
                    eventHandler_closure_t fireData)
{
  onData_t oData = (onData_t) onData;
  fireData_t fData = (fireData_t) fireData;
  launchpad_data_t *btn_info = oData->btn_info;
  int pid;
  if ((pid = fork()) == 0) {
    if (btn_info->startDirectory) {
      chdir(btn_info->startDirectory);
    }
    execvp(btn_info->cmd, btn_info->args);
    perror(btn_info->cmd);
    exit(0);
  }
  int status;
  while ((waitpid(pid, &status, 0) == 0));
  LOCK(fData->lock);
  *fData->runPost += 1;
  UNLOCK(fData->lock);
}

static int preFireFn(eventName_t eventName,
                      eventHandler_closure_t onData,
                      eventHandler_closure_t fireData)
{
  fireData_t fData = (fireData_t) fireData;
  onData_t oData = (onData_t) onData;
  LOCK(fData->lock);
  int num_remaining = *(fData->num_remaining);
  if (num_remaining == 0) {
    UNLOCK(fData->lock);
    return 1;
  }else {
    num_remaining = --*(fData->num_remaining);
    int num_active = ++fData->num_active[oData->index];
    GtkWidget *label = g_object_get_data(G_OBJECT(oData->button), "label");
    setGuiCount(fData->footage, label, num_remaining, num_active);
    UNLOCK(fData->lock);
    return 0;
  }
}

static int postFireFn(eventName_t eventName,
                      eventHandler_closure_t onData,
                      eventHandler_closure_t fireData)
{
  fireData_t fData = (fireData_t) fireData;
  onData_t oData = (onData_t) onData;
  LOCK(fData->lock);
  if (*fData->runPost != 0) {
    int num_remaining = ++*(fData->num_remaining);
    int num_active = --fData->num_active[oData->index];
    GtkWidget *label = g_object_get_data(G_OBJECT(oData->button), "label");
    setGuiCount(fData->footage, label, num_remaining, num_active);
    *fData->runPost -= 1;
  }
  UNLOCK(fData->lock);
  return 0;
}

static void fireEvent(GtkWidget *widget,
                      gpointer fireData)
{
  eventHandler_closure_t fData = (fireData_t) fireData;
  char *name = (char *) g_object_get_data(G_OBJECT(widget), "name");
  eventManager_t mgr = (eventManager_t) g_object_get_data(G_OBJECT(widget), "mgr"); 
  eventManager_fireEvent(mgr, name, fData, preFireFn, postFireFn, NULL);
}

static gboolean delete_event(GtkWidget *widget,
                        gpointer data)
{
  gtk_main_quit();
  return FALSE;
}

int main(int argc, char *argv[])
{
  int num_btns = getNButtons();
  char cwd[1024];
  char *cmd;

  // get command name
  char *pch;
  pch = strtok(argv[0], "/");
  while (pch != NULL) {
    cmd = pch;
    pch = strtok(NULL, "/");
  }
  
  // get current directory
  if (getcwd(cwd, sizeof(cwd)) == NULL) {
    fprintf(stderr, "Can't get current directory\n");
    exit(1);
  }
  
  g_thread_init(NULL);
  gdk_threads_init();
  gtk_init(&argc, &argv);

  gdk_threads_enter();
  // initialize window
  GtkWidget *window;
  window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
  gtk_window_set_title(GTK_WINDOW(window), cmd);
  GdkColor blue = {0, 0x8FFF, 0x8FFF, 0xFFFF};
  gtk_widget_modify_bg(window, GTK_STATE_NORMAL, &blue);
  g_signal_connect(window, "delete-event",
                  G_CALLBACK(delete_event), NULL);
  gtk_container_set_border_width(GTK_CONTAINER(window), 10);
  GtkWidget *vbox = gtk_vbox_new(FALSE, 0);
  gtk_container_add(GTK_CONTAINER(window), vbox);

  // set label for current directory
  GtkWidget *hbox_dir = gtk_hbox_new(FALSE, 0);
  GtkWidget *hbox_foot = gtk_hbox_new(FALSE, 0);
  GtkWidget *cur_dir = gtk_label_new(cwd);
  char buf[20];
  sprintf(buf, "%d launches remaining", NWORKERS);
  GtkWidget *footage = gtk_label_new(buf);
  gtk_box_pack_start(GTK_BOX(hbox_dir), cur_dir, FALSE, FALSE, 0);
  gtk_box_pack_start(GTK_BOX(vbox), hbox_dir, FALSE, FALSE, 0);
  gtk_box_pack_start(GTK_BOX(hbox_foot), footage, FALSE, FALSE, 0);
  gtk_widget_show(cur_dir);
  gtk_widget_show(hbox_dir);
  gtk_widget_show(footage);
  gtk_widget_show(hbox_foot);

  // create buttons and lables show the count of clicks
  eventManager_t mgr;
  eventManager_create(&mgr, NWORKERS + 1);
  fireData_t fireData;
  fireData = fireData_create(NWORKERS, num_btns, footage);
  int i;
  launchpad_data_t **btns = getButtons();
  GtkWidget *hbox = gtk_hbox_new(FALSE, 0);
  gtk_box_pack_start(GTK_BOX(vbox), hbox, FALSE, FALSE, 0);
  for (i = 0; i < num_btns; i++) {
    launchpad_data_t *btn_info = btns[i];
    GtkWidget *vbox_btn_label = gtk_vbox_new(FALSE, 0);
    GtkWidget *button = gtk_button_new_with_label(btn_info->buttonLabel);
    GtkWidget *label = gtk_label_new("0 active");
    onData_t onData = onData_t_acquire();
    onData->index = i;
    onData->button = button;
    onData->btn_info = btn_info;
    eventManager_onEvent(mgr, btn_info->buttonLabel, handler, onData, onDataRelease); 
    g_object_set_data(G_OBJECT(button), "mgr", (gpointer) mgr);
    g_object_set_data(G_OBJECT(button), "name", (gpointer) btn_info->buttonLabel);
    g_object_set_data(G_OBJECT(button), "label", (gpointer) label);
    g_signal_connect(button, "clicked",
                  G_CALLBACK(fireEvent), fireData);
    gtk_box_pack_start(GTK_BOX(vbox_btn_label), button, FALSE, FALSE, 0);
    gtk_box_pack_start(GTK_BOX(vbox_btn_label), label, FALSE, FALSE, 0);
    gtk_box_pack_start(GTK_BOX(hbox), vbox_btn_label, FALSE, FALSE, 20);
    gtk_widget_show(button);
    gtk_widget_show(label);
    gtk_widget_show(vbox_btn_label);
  }
  gtk_widget_show(hbox);
  gtk_box_pack_start(GTK_BOX(vbox), hbox_foot, FALSE, FALSE, 0);
  gtk_widget_show(vbox);
  gtk_widget_show(window);
  gdk_threads_leave();

  gtk_main();
  fireData_t_release(fireData); 
  return 0;
}
