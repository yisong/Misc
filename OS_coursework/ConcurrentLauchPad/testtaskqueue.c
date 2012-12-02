// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: testtaskqueue.c,v 1.1 2011/10/23 20:10:28 zahorjan Exp $

#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

#include "taskQueue.h"

#define NTHREADS 2
#define NTASKWORKERS 2

static pthread_barrier_t startBarrier;
static taskQueue_t tq;
static int tCount[NTHREADS];


EXPOSETYPE(taskData_t)
INSTANTIATETYPE(taskData_t, int i;
               )
CREATEMANAGER( taskData_t, NULL )

/************************************************
 * taskFn() -- invoked by taskQueue
 ************************************************/

taskQueue_taskResult_t taskFn(taskQueue_closure_t c) {
  return NULL;
}

/************************************************
 * taskDoneFn() -- invoked by taskQueue
 ************************************************/

void taskDoneFn(taskQueue_closure_t c, taskQueue_taskResult_t taskResult) {
  taskData_t_release( (taskData_t)c );
}


/************************************************
 * workerFn() -- local thread workload generation
 ************************************************/

void* workerFn(void* arg) {
  long tid = (long)arg;

  pthread_barrier_wait( &startBarrier );

  int i;
  for (i=0; i<25000; i++) {
    taskData_t arg = taskData_t_acquire();
    arg->i = i;
    taskQueue_addTask( tq, taskFn, arg, taskDoneFn );
    tCount[tid]++;
  }

  return NULL;
}


/************************************************
 * main
 ************************************************/

int main(int argc, char* argv[]) {
  long t;
  pthread_t thread[NTHREADS];

  taskQueue_create(&tq, NTASKWORKERS);

  pthread_barrier_init( &startBarrier, NULL, NTHREADS + 1 );

  for (t=0; t<NTHREADS; t++) {
    tCount[t] = 0;
    pthread_create(&thread[t], NULL, workerFn, (void*)t );
  }

  pthread_barrier_wait( &startBarrier );
  for (t=0; t<NTHREADS; t++) {
    pthread_join(thread[t], NULL);
  }

  for (t=0; t<NTHREADS; t++ ) {
    printf("%ld: %d\n", t, tCount[t]);
  }

  pthread_barrier_destroy( &startBarrier );
  taskQueue_destroy( tq );

  return 0;

}
