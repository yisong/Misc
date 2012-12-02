// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: testeventmanager.c,v 1.1 2011/10/23 20:10:28 zahorjan Exp $

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>

#include "eventManager.h"
#include "factory.h"

// set NWORKERS to 0 to get single-threaded (synchronous) execution
// of event handlers -- at least in the solution code
#define NWORKERS 10

#define NTESTS 10000
static int testCnt[3][NTESTS];

/************************************************
 * create two types (structs), and handler code
 * for them.
 ************************************************/

EXPOSETYPE(onData_t)
INSTANTIATETYPE(onData_t, char* eventName;
                          int id;
               )
void onDataDestructor( onData_t onData ) {
  if ( onData->eventName ) free( onData->eventName );
}
CREATEMANAGER( onData_t, onDataDestructor )
void onDataRelease( eventHandler_closure_t onData) {
  onData_t_release( (onData_t)onData );
}

EXPOSETYPE(fireData_t)
INSTANTIATETYPE(fireData_t, int id; )
CREATEMANAGER( fireData_t, NULL )

/************************************************
 * ref counted "objects" event handler
 ************************************************/

// pre/post-fire callbacks

int preFireCallback( eventName_t eventName,
                     eventHandler_closure_t onData,
                     eventHandler_closure_t fireData ) {
  fireData_t_ref( (fireData_t)fireData);
  return 0;
}

int postFireCallback( eventName_t eventName,
                      eventHandler_closure_t onData,
                      eventHandler_closure_t fireData ) {
  fireData_t_release( (fireData_t)fireData );
  return 0;
}

void allDoneCallBack( eventName_t eventName,
                      eventHandler_closure_t fireData ) {
  fireData_t_release( (fireData_t)fireData );
}


// the handler body
void handler(eventHandler_closure_t onData, eventHandler_closure_t fireData) {
  onData_t onD = (onData_t)onData;
  fireData_t fireD = (fireData_t)fireData;

  testCnt[onD->id][fireD->id] = 1;
}

/************************************************
 * A more typical form for the handler
 ************************************************/

void staticHandler(eventHandler_closure_t onData, eventHandler_closure_t fireData) {
  assert( 10 == (long)onData && 20 == (long)fireData );
}

/************************************************
 * main
 ************************************************/

int main(int argc, char* argv[]) {

  int i, j;
  for ( i=0; i<3; i++ ) {
    for ( j=0; j<NTESTS; j++ ) {
      testCnt[i][j] = 0;
    }
  }

  printf("\n%d tests, %d workers\n\n", NTESTS, NWORKERS);

  eventManager_t manager;
  eventManager_create( &manager, NWORKERS );

  onData_t onData;
  onData = onData_t_acquire();
  onData->eventName = strdup("test1");
  onData->id = 0;

  eventManager_onEvent( manager, "test1", handler, onData, onDataRelease  );

  onData = onData_t_acquire();
  onData->eventName = strdup("test2");
  onData->id = 1;

  eventManager_onEvent( manager, "test2", handler, onData, onDataRelease  );

  onData = onData_t_acquire();
  onData->eventName = strdup("test2");
  onData->id = 2;

  eventManager_onEvent( manager, "test2", handler, onData, onDataRelease  );

   // an example of a handler that doesn't expect ref counted "object" arguments
  eventManager_onEvent( manager, "test3", staticHandler, (eventHandler_closure_t)10, NULL );


  fireData_t fireData;
  int test;
  for ( test = 0; test < NTESTS; test++ ) {
    fireData = fireData_t_acquire();
    fireData->id = test;
    fireData_t_ref(fireData); // this has to precede the first call to fireEvent() or there's a race
    eventManager_fireEvent( manager, "test1", fireData, preFireCallback, postFireCallback, allDoneCallBack );
    eventManager_fireEvent( manager, "test2", fireData, preFireCallback, postFireCallback, allDoneCallBack );

    eventManager_fireEvent( manager, "test3", (eventHandler_closure_t)20, NULL, NULL, NULL );
  }

  // this call will block until all queued events have been handled
 eventManager_destroy( manager ); 

  // check results
  printf("Checking results.\n(No news is good news.)\n");
  for ( i=0; i<3; i++ ) {
    for ( j=0; j<NTESTS; j++ ) {
      if ( testCnt[i][j] != 1 ) printf("[%d][%d] = %d\n", i, j, testCnt[i][j]);
    }
  }

  return 0;

}
