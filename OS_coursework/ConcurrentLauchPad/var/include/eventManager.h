// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: eventManager.h,v 1.2 2011/10/24 01:34:38 zahorjan Exp $

/*****************************************************************
 * The event system
 *****************************************************************/

#ifndef EVENTMANAGER_H
#define EVENTMANAGER_H

#include "factory.h"

typedef char* eventName_t;

typedef void* eventHandler_closure_t;
typedef void (*eventHandler_fn_t)( eventHandler_closure_t onData,
                                   eventHandler_closure_t fireData );

EXPOSETYPE(eventManager_t)

// returns 0 for success
extern int eventManager_create( eventManager_t* mgr,   // the newly constructed eventManager
                                int nWorkers);         // the number of worker threads in the taskQueue it's built upon
                                                       // if 0, have the thread calling fireEvent execute all the
                                                       // event handlers immediately

// Once this call is made, no new handlers can be registerd, and no new events fired.
// handlers in execution, or awaiting execution because of an already executed fireEvent(), are
// completed before destruction takes place.
// Returns 0 for success.  Results are undefined if it fails.
extern int eventManager_destroy( eventManager_t mgr);  // releases all memory, including that consumed by registered handlers


// Returns 0 for success.
// The onDataDestructorFn will be called when this eventManager is destroy'ed, if not NULL.
// (When called, its argument is the onData being registered here.)
typedef void (*eventManager_onData_destructor_t)(eventHandler_closure_t);
extern int eventManager_onEvent( eventManager_t mgr,
                                 eventName_t eventName,
                                 eventHandler_fn_t fn,           // the handler function
                                 eventHandler_closure_t onData,  // data to be passed to it each time this event is fired
                                 eventManager_onData_destructor_t onDataDestructorFn );



// eventManager_fireEvent() causes all handlers registered for this event to be called.
// Each such invocation provides the onData argument given when onEvent was called, and
// the fireData argument given in this fireEvent.
//
// See the assignment writeup for more details on the use of the three callbacks.
typedef int (*eventManager_firecallback_fn_t)( eventName_t eventName,
                                               eventHandler_closure_t onData,
                                               eventHandler_closure_t fireData );
typedef void (*eventManager_allfiredcallback_fn_t)( eventName_t eventName,
                                                    eventHandler_closure_t fireData );
extern void eventManager_fireEvent( eventManager_t mgr,
                                    eventName_t eventName,
                                    eventHandler_closure_t fireData,
                                    eventManager_firecallback_fn_t preFireFn,      // Called before each handler is registered for execution.
                                                                                   // If return value is non-zero, execution of that (one) handler is skipped
                                    eventManager_firecallback_fn_t postFireFn,     // Called after each handler has executed.  Return value is ignored.
                                    eventManager_allfiredcallback_fn_t allFiredFn  // Called when all handlers triggered by this fireEvent have finished executing

                                    );

#endif // EVENTMANAGER_H
