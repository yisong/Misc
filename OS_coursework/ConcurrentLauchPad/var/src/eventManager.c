// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: eventManager.c,v 1.1 2011/10/24 01:34:38 zahorjan Exp $

#include <errno.h>
#include <pthread.h>
#include <signal.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

#include "eventManager.h"
#include "hashTable.h"
#include "lockDef.h"
#include "taskQueue.h"
#include "queue.h"

#define HASHTABLE_SIZE_DEFAULT 31

INSTANTIATETYPE(eventManager_t, taskQueue_t tq; hashTable_t hash;)

EXPOSETYPE ( on_fire_data_t )
INSTANTIATETYPE (on_fire_data_t, eventHandler_closure_t onData; eventHandler_closure_t fireData;
                 eventHandler_fn_t fn; eventName_t eventName; eventManager_firecallback_fn_t preFireFn;
                 eventManager_firecallback_fn_t postFireFn; eventManager_allfiredcallback_fn_t allFiredFn;
                 LOCK_t* l; int* num;)
CREATEMANAGER (on_fire_data_t, NULL )

typedef struct eventHandler_t {
  eventHandler_fn_t fn;
  eventHandler_closure_t onData;
  eventManager_onData_destructor_t onDataDestructorFn;
}*eventHandler_t;

typedef struct queue_closure_t{
  int *num;
  LOCK_t *lock;
  taskQueue_t tq;
  eventHandler_closure_t fireData;
  eventName_t eventName;
  eventManager_firecallback_fn_t preFireFn;
  eventManager_firecallback_fn_t postFireFn;
  eventManager_allfiredcallback_fn_t allFiredFn;
}*queue_closure_t;

static int hash_fn(eventName_t str);
static int key_cmp_fn(eventName_t str1, eventName_t str2);
static void eventHandler_t_destroy(eventHandler_t ehr);
static void eventManager_internal_destroy(eventManager_t mgr);
static taskQueue_taskResult_t handlerWrapper(taskQueue_closure_t data);
static void postCallbackWrapper(taskQueue_closure_t data, taskQueue_taskResult_t res);
static boolean_t q_apply(queue_element_t ele, queue_pfapply_closure_t data);

int eventManager_create(eventManager_t *mgr,
                        int nWorkers)
{
  eventManager_t newMgr = eventManager_t_acquire();
  if (nWorkers != 0) {
    if ( taskQueue_create(&newMgr->tq, nWorkers)) {
      fprintf(stderr, "error on creating eventManager\n");
      return 1;
    }
  }
  newMgr->hash = hashTable_create(HASHTABLE_SIZE_DEFAULT, (hash_fn_t) hash_fn, 
                                  (hash_key_cmp_fn_t) key_cmp_fn,
                                  free, (hash_value_destroy_fn_t) queue_destroy);
  if ( !newMgr->hash ) {
    fprintf(stderr, "error on creating hashtable\n");
    return 1;
  }
  *mgr = newMgr;
  return 0;
}

int eventManager_destroy(eventManager_t mgr)
{
  eventManager_t_release(mgr);
  return 0;
}

static void eventManager_internal_destroy(eventManager_t mgr)
{
  taskQueue_destroy(mgr->tq);
  hashTable_destroy(mgr->hash);
}

CREATEMANAGER(eventManager_t, eventManager_internal_destroy)

int eventManager_onEvent(eventManager_t mgr,
                        eventName_t eventName,
                        eventHandler_fn_t fn,
                        eventHandler_closure_t onData,
                        eventManager_onData_destructor_t onDataDestructorFn)
{
  // create an eventHandler
  eventHandler_t ehr = (eventHandler_t) malloc(sizeof(struct eventHandler_t));
  if (ehr == NULL) {
    fprintf(stderr, "error on mallocing eventHandler_t\n");
    return 1;
  }
  ehr->fn = fn;
  ehr->onData = onData;
  ehr->onDataDestructorFn = onDataDestructorFn;

  // add eventHandler and name to hashtable in eventManager
  queue_t q;
  if ((q = hashTable_lookupItem(mgr->hash, eventName)) == NULL) {
    q = queue_create((queue_element_pfdestroy_t) eventHandler_t_destroy);
    if (q == NULL) {
      fprintf(stderr, "error on creating queue\n");
      return 1;
    }

    char *name = strdup(eventName); 
    if (hashTable_addItem(mgr->hash, name, q)) {
      fprintf(stderr, "error on adding item to hashtable\n");
      return 1;
    }
  } 
  queue_append(q, ehr);
  return 0;
}

void eventManager_fireEvent(eventManager_t mgr,
                            eventName_t eventName,
                            eventHandler_closure_t fireData,
                            eventManager_firecallback_fn_t preFireFn,
                            eventManager_firecallback_fn_t postFireFn,
                            eventManager_allfiredcallback_fn_t allFiredFn)
{
  queue_t q = hashTable_lookupItem(mgr->hash, eventName);
  if (!q) return;

  int size = queue_size(q);
  LOCK_t* lock = (LOCK_t*)malloc(sizeof(LOCK_t));
  LOCK_INIT(lock);
  int* num = (int*)malloc(sizeof(int));
  *num = size;
  
  struct queue_closure_t d;
  d.num = num;
  d.eventName = eventName;
  d.preFireFn = preFireFn;
  d.postFireFn = postFireFn;
  d.allFiredFn = allFiredFn;
  d.lock = lock;
  d.fireData = fireData;
  d.tq= mgr->tq;
  
  queue_apply(q, q_apply, &d);
}

// using djb2 hash function
static int hash_fn(eventName_t str)
{
  char *name = (char *)str;
  uint32_t hash = 5381;
  int c;

  while ((c = *name++))
    hash = ((hash << 5) + hash) + c;
  return hash;
}

static int key_cmp_fn(eventName_t str1, eventName_t str2)
{
  char *name1 = (char *)str1;
  char *name2 = (char *)str2;
  int c1;
  int c2;
  while(1) {
    c1 = *name1++;
    c2 = *name2++;
    if (c1 != c2) {
      return c1 - c2;
    }
    if ((c1 == 0) && (c2 == 0)) {
      return 0;
    }
  }
}

static void eventHandler_t_destroy(eventHandler_t ehr)
{
  if (ehr->onDataDestructorFn) {
    ehr->onDataDestructorFn(ehr->onData);
  }
  free(ehr);
}

static void postCallbackWrapper(taskQueue_closure_t data, taskQueue_taskResult_t taskResult)
{
  on_fire_data_t d = (on_fire_data_t) data;
  LOCK((d->l));
  *(d->num) = *(d->num) - 1;
  if (*(d->num) == 0) {
    if (d->allFiredFn) d->allFiredFn(d->eventName, d->fireData);
    free(d->num);
    UNLOCK((d->l));
    LOCK_DESTROY((d->l));
    free(d->l);
  } else {
    UNLOCK((d->l));
  }
  on_fire_data_t_release(d);
}

static taskQueue_taskResult_t handlerWrapper(taskQueue_closure_t data)
{
  on_fire_data_t d = (on_fire_data_t) data;
  if (!(d->preFireFn && d->preFireFn( d->eventName, d->onData, d->fireData))) {
    d->fn(d->onData, d->fireData);
  }
  if (d->postFireFn) {
    d->postFireFn(d->eventName, d->onData, d->fireData);
  } 
 return NULL;
}



static boolean_t q_apply(queue_element_t ele, queue_pfapply_closure_t data)
{
  eventHandler_t ehr;
  queue_closure_t d;

  ehr = (eventHandler_t) ele;
  d = (queue_closure_t) data;

  on_fire_data_t onFireData = on_fire_data_t_acquire();
  onFireData->onData = ehr->onData;
  onFireData->fireData = d->fireData;
  onFireData->fn = ehr->fn;
  onFireData->eventName = d->eventName;
  onFireData->preFireFn = d->preFireFn;
  onFireData->postFireFn = d->postFireFn;
  onFireData->allFiredFn = d->allFiredFn;
  onFireData->l = d->lock;
  onFireData->num = d->num;

  taskQueue_addTask(d->tq, handlerWrapper, (void*)onFireData, postCallbackWrapper);
  return TRUE;
}
