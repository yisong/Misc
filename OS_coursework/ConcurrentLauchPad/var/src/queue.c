// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: queue.c,v 1.1 2011/10/24 01:34:38 zahorjan Exp $

/* Implements queue abstract data type. */

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include "lockDef.h"
#include "queue.h"

typedef struct queue_node_t *queue_node_t;

struct queue_node_t {
  queue_element_t e;
  queue_node_t next;
};
  
INSTANTIATETYPE( queue_t, queue_element_pfdestroy_t fn; queue_node_t head;
                 queue_node_t tail; int size; LOCK_t l; LOCK_t ls;)

static queue_node_t queue_new_element(queue_element_t e);

queue_t queue_create ( queue_element_pfdestroy_t elementDestructor) {

  queue_t q = queue_t_acquire();
  if (!q ) return NULL;
  q -> head = NULL;
  q -> tail = NULL;
  q -> fn = elementDestructor;
  q -> size = 0;
  LOCK_INIT(&(q->l));
  LOCK_INIT(&(q->ls));
  return q;
}

void queue_destroy (queue_t q) {
  queue_t_release( q );
}

static void queue_internal_destroy (queue_t q) {
  queue_element_t e;
  while (queue_remove(q, &e)) {
    if (q -> fn) (q -> fn)(e);
  }
  LOCK_DESTROY(&(q->l));  
  LOCK_DESTROY(&(q->ls));
}  

CREATEMANAGER( queue_t , queue_internal_destroy )

void queue_append (queue_t q, queue_element_t e) {
  queue_node_t t = queue_new_element(e);

  assert( q != NULL );

  LOCK(&(q->ls));
  q -> size += 1;
  UNLOCK(&(q->ls));
  
  LOCK(&(q->l));
  if (q->head == NULL) {
    q->head = t;
    q->tail = t;
    UNLOCK(&(q->l));
    return;
  }

  q -> tail-> next = t;
  q -> tail = q -> tail -> next;
  UNLOCK(&(q->l));
}

boolean_t queue_remove (queue_t q, queue_element_t* e ) {
  queue_node_t oldHead;
  assert(q != NULL);
	
  LOCK(&(q->l));

  if (q->head == NULL) {
    UNLOCK(&(q->l));
    return FALSE;
  }

  *e = q->head->e;
  oldHead = q->head;
  q->head = q->head->next;
  UNLOCK(&(q->l));

  free(oldHead);

  LOCK(&(q->ls));
  q -> size -= 1;
  UNLOCK(&(q->ls));

  return TRUE;
}

boolean_t queue_is_empty(queue_t q) {

  return (q->head == NULL);
}

int queue_size (queue_t q) {
  return q -> size;
}

static queue_node_t queue_new_element(queue_element_t e) {
  queue_node_t n = (queue_node_t)malloc(sizeof(struct queue_node_t));
  if ( !n ) return NULL;
  n -> e = e;
  n -> next = NULL;
  return n;
}

boolean_t queue_apply(queue_t q, queue_pfapply_t pf, queue_pfapply_closure_t cl) 
{
  assert(q != NULL && pf != NULL);

  LOCK(&(q->l));
  
  if (q->head==NULL) {
    UNLOCK(&(q->l));
     return FALSE;
  }
  queue_node_t cur;
  for (cur = q->head; cur; cur = cur->next) {
    if (!pf(cur->e, cl)) {
      UNLOCK(&(q->l));
      return TRUE;
    }
  }
  UNLOCK(&(q->l));
  return TRUE;
}
