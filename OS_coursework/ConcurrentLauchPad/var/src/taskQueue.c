// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-

#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#include "taskQueue.h"
#include "lockDef.h"
#include "queue.h"


INSTANTIATETYPE ( taskQueue_t, int num; int killthreads; queue_t q;
                  LOCK_t l; pthread_cond_t cond; pthread_t* thread_array;)

EXPOSETYPE ( task_t )
INSTANTIATETYPE ( task_t, taskQueue_task_fn_t f; taskQueue_closure_t c;
                  taskQueue_taskDone_fn_t doneFunc; )
CREATEMANAGER ( task_t, NULL )


static void* threadloop (void* arg);  

int taskQueue_create ( taskQueue_t* tqp, int nWorkers) {
  taskQueue_t tq = taskQueue_t_acquire();
  if ( !tq ) return -1;
  tq -> num = nWorkers;
  tq -> thread_array = malloc(sizeof(pthread_t) * nWorkers);
  if ( !(tq -> thread_array) ) return -1;
  tq -> q = queue_create((queue_element_pfdestroy_t)task_t_release);
  LOCK_INIT(&(tq -> l));
  pthread_cond_init(&(tq -> cond), 0);
  tq -> killthreads = 0;
  int i;
  for ( i = 0; i < nWorkers; i++) {
    pthread_create(&(tq -> thread_array[i]), NULL, threadloop, tq);
  }
  *tqp = tq;
  return 0; 
}

static void taskQueue_internal_destroy(taskQueue_t tq) {
  LOCK(&(tq -> l));
  tq -> killthreads = 1;
  UNLOCK(&(tq -> l));
  pthread_cond_broadcast(&(tq -> cond));
  int i;
  for ( i = 0; i < tq -> num; i++) {
    pthread_join(tq -> thread_array[i], NULL);
  }
  free(tq -> thread_array);
  queue_destroy(tq -> q);
  LOCK_DESTROY(&(tq -> l));
  pthread_cond_destroy(&(tq -> cond));
}

CREATEMANAGER ( taskQueue_t, taskQueue_internal_destroy )

void taskQueue_destroy(taskQueue_t tq) {
  taskQueue_t_release(tq);
}

int taskQueue_addTask(taskQueue_t tq, taskQueue_task_fn_t f,
                      taskQueue_closure_t c, taskQueue_taskDone_fn_t doneFunc) {
  LOCK(&(tq -> l));
  if (tq -> killthreads) {
    UNLOCK(&(tq -> l));
    return -1;
  }
  UNLOCK(&(tq -> l));

  if (tq -> num == 0) {
    taskQueue_taskResult_t res = f(c);
    if (doneFunc) doneFunc(c, res);
    return 0;
  }

  task_t t = task_t_acquire();
  if ( !t ) return -1;
  t -> f = f;
  t -> c = c;
  t -> doneFunc = doneFunc;
  LOCK(&(tq -> l));
  queue_append (tq -> q, t ); 
  pthread_cond_signal(&(tq -> cond));
  UNLOCK(&(tq -> l));
  return 0;
}

static void* threadloop (void* arg) {
  taskQueue_t tq = (taskQueue_t)arg;
  task_t t;

  while ( 1 ) {
    LOCK(&(tq -> l));
    while ( !queue_remove(tq -> q, (void *)&t)) {
      if ( tq -> killthreads) {
        UNLOCK(&(tq -> l));
        return NULL;
      }
      pthread_cond_wait(&(tq -> cond), &(tq -> l));
    }

    UNLOCK(&(tq -> l));
    taskQueue_taskResult_t res = (t-> f)(t -> c);
    if (t -> doneFunc) (t-> doneFunc)(t -> c, res);
    task_t_release(t);
  }
  return NULL;
}
