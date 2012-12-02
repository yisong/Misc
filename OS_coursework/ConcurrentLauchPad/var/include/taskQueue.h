// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: taskQueue.h,v 1.1 2011/10/23 20:10:29 zahorjan Exp $

/**********************************************************
 * task queue interface
 **********************************************************/

#ifndef TASKQUEUE_H
#define TASKQUEUE_H

#include "factory.h"

//------------------------------------------------------
// This macro does a typedef for the type (taskQueue_t)
// AND exposes methods to acquire (taskQueue_t_acquire()),
// release (taskQueue_t_release(taskQueue_t)), and 
// increment the reference count (taskQueue_t_ref(taskQueue_t)
// taskQueue_t "objects"
//------------------------------------------------------

EXPOSETYPE(taskQueue_t)

//------------------------------------------------------
// callback types
//------------------------------------------------------

typedef void* taskQueue_closure_t;
typedef void* taskQueue_taskResult_t;

typedef taskQueue_taskResult_t (*taskQueue_task_fn_t)(taskQueue_closure_t c);
typedef void (*taskQueue_taskDone_fn_t)(taskQueue_closure_t c,
                                        taskQueue_taskResult_t taskResult);  // result returned by task function invocation

//------------------------------------------------------
// taskQueue_t interface
//------------------------------------------------------

// returns 0 for success
extern int taskQueue_create( taskQueue_t* tqp,
                             int nWorkers);     // number of worker threads to use
                                                // if 0, the thread invoking addTask
                                                //   immediately executes the task, rather than
                                                //   enqueueing it.

// when destroy is invoked, no new tasks can be
// added.  Any tasks already in the queue, or in
// execution, are completed.  This call doesn't
// return until all worker threads have terminated.
extern void taskQueue_destroy(taskQueue_t tq);

// returns 0 for success
extern int taskQueue_addTask(taskQueue_t tq,
                             taskQueue_task_fn_t f,
                             taskQueue_closure_t c,
                             taskQueue_taskDone_fn_t doneFunc);  // if not NULL, invoked when task completes.

#endif // TASKQUEUE_H
