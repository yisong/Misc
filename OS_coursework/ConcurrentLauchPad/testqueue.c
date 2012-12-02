// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: testqueue.c,v 1.2 2011/10/24 04:11:29 zahorjan Exp $

#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

#include "queue.h"

/********************************************
 * globals
 ********************************************/

#define NTHREADS 2

pthread_barrier_t startBarrier;

queue_t q;
int tCount[NTHREADS];

/************************************************
 * Creation of a useless type, to show usage
 * of the macros (in factory.h)
 ************************************************/

EXPOSETYPE(demo_t)

demo_t sharedDemo_t;

INSTANTIATETYPE(demo_t, demo_t savedDemo_t; )

void demo_t_init(demo_t e) {
  e->savedDemo_t = sharedDemo_t;
  demo_t_ref(sharedDemo_t);
}

void demo_t_destroy(demo_t e) {
  if ( e->savedDemo_t) demo_t_release(e->savedDemo_t);  // destructor was NULL
}

CREATEMANAGER(demo_t, demo_t_destroy)

/************************************************
 * workerFn()
 ************************************************/

void* workerFn(void* arg) {
  long t = (long)arg;
  int i, j;

  pthread_barrier_wait( &startBarrier );

  queue_element_t e;
  for (i=0; i<250000; i++) {
    for (j=0; j<4; j++) {
      demo_t dummy = demo_t_acquire();  // get a new, uninitialized one
      demo_t_init( dummy );
      queue_append( q, dummy );
    }
    // we want to leave some in the queue, to check if queue_destroy()
    // cleans up properly
    if ( i != 0 ) {
      for (j=0; j<4; j++) {
        queue_remove( q, &e );
        demo_t_release( (demo_t)e  );
      }
    }
    tCount[t] += 4;
  }

  return NULL;
}

/************************************************
 * destroy_element()
 * callback function given to queue_destroy
 * (to destroy elements still in the queue)
 ************************************************/

void destroy_element(demo_t e) {
  demo_t_release( e );
}

/************************************************
 * main
 ************************************************/

int main(int argc, char* argv[]) {
  long t;
  pthread_t thread[NTHREADS];

  sharedDemo_t = demo_t_acquire();
  sharedDemo_t->savedDemo_t = NULL;

  q = queue_create((queue_element_pfdestroy_t)destroy_element);
  queue_t queueCopy = q;
  queue_t_ref(q);

  pthread_barrier_init( &startBarrier, NULL, NTHREADS + 1 );

  for (t=0; t<NTHREADS; t++) {
    tCount[t] = 0;
    pthread_create(&thread[t], NULL, workerFn, (void*)t );
  }

  // try to make all worker threads work at the same time
  pthread_barrier_wait( &startBarrier );

  // now wait for them all to finish
  for (t=0; t<NTHREADS; t++) {
    pthread_join(thread[t], NULL);
  }

  // print some summary statistics.
  // We're expecting the queue size to be NTHREADS*4
  printf("\nQueue size = %d\n", queue_size(q) );
  if ( queue_size(q) == NTHREADS*4 ) printf("...Looks good\n\n");
  else printf("...Should be %d\n\n", NTHREADS*4 );

  for (t=0; t<NTHREADS; t++ ) {
    printf("%ld: %d\n", t, tCount[t]);
  }

  // clean up

  pthread_barrier_destroy( &startBarrier );

  queue_destroy(q);


  queue_destroy(queueCopy);

  demo_t_release(sharedDemo_t);
  return 0;
}
