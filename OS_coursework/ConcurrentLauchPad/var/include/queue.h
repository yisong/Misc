// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: queue.h,v 1.2 2011/10/24 04:11:29 zahorjan Exp $

#ifndef _QUEUE_H
#define _QUEUE_H

#include "factory.h"

#ifndef FALSE
#define FALSE 0
#define TRUE (!FALSE)
#endif // FALSE
typedef int boolean_t;

/*
 * This macro typedef's the type (queue_t) as an opaque pointer type
 */
EXPOSETYPE( queue_t )

typedef void *queue_element_t;
typedef void (*queue_element_pfdestroy_t)(queue_element_t);  // if a non-empty queue is destroyed, this client-supplied
                                                             // callback is invoked to let the client figure out how/if to
                                                             // destroy each data element

/*
 * Creates and returns a new queue.
 * Returns null on failure, else a valid queue.
 */
extern queue_t queue_create( queue_element_pfdestroy_t elementDestructor );

// destroys the queue.  destroying a non-empty queue is legal.
extern void queue_destroy( queue_t q );

/*
 * Appends an element to the end of the queue.
 */

extern void queue_append(queue_t q, queue_element_t e );

/* Remove the first element from the queue and leaves result in *e.
 *   Returns false if isEmpty() prior to remove.
 */
extern boolean_t queue_remove(queue_t q, queue_element_t* e);

// just as in Project 0...

extern boolean_t queue_is_empty(queue_t q);
extern int queue_size(queue_t q);


typedef void *queue_pfapply_closure_t;
typedef boolean_t (queue_pfapply_t)(queue_element_t, queue_pfapply_closure_t);
extern boolean_t queue_apply(queue_t q, queue_pfapply_t pf, queue_pfapply_closure_t cl);


#endif //_QUEUE_H


