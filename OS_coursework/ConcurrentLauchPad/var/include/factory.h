// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: factory.h,v 1.1 2011/10/23 20:10:29 zahorjan Exp $

#ifndef FACTORY_H
#define FACTORY_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>

#include "lockDef.h"

#define EXPOSETYPE(typename) typedef struct typename* typename; \
  extern int typename ## _ref( typename foo );                  \
  extern typename typename ## _acquire();                       \
  extern int typename ## _release(typename);


#define INSTANTIATETYPE(typename, subclassdef)  \
  struct typename {                             \
     typename ar_next;                          \
     int      ar_refCnt;                        \
     int      ar_state;                         \
     LOCK_t   ar_lock;                          \
     const char* ar_typename;                   \
     subclassdef                                \
  };
  

#define CREATEMANAGER(typename, destruct  )     \
  typedef struct {                              \
     typename head;                             \
     pthread_mutex_t lock;                      \
     int isRegistered;                          \
     void (*destructor)(typename);              \
  } typename ## _manager;                       \
  typename ## _manager typename ## _Manager =   \
    { NULL, PTHREAD_MUTEX_INITIALIZER, 0, destruct};   \
  void typename ## _Manager_destroy() {         \
    typename el;                                \
    pthread_mutex_lock( & typename ## _Manager.lock ); \
    while ( (el= typename ## _Manager.head) ) { \
      typename ## _Manager.head = el->ar_next;  \
      LOCK_DESTROY( &el->ar_lock );             \
      free(el);                                 \
    }                                           \
    pthread_mutex_unlock( & typename ## _Manager.lock ); \
  }                                             \
  typename typename ## _acquire() {             \
    pthread_mutex_lock( & typename ## _Manager.lock ); \
    if ( ! typename ## _Manager.isRegistered ) {\
      atexit( typename ##_Manager_destroy );    \
      typename ## _Manager.isRegistered = 1;    \
    }                                           \
    typename foo;                               \
    if ( ! typename ## _Manager.head ) {        \
      foo = (typename)malloc(sizeof(struct typename)); \
      LOCK_INIT(&foo->ar_lock);                 \
      foo->ar_typename = #typename ;             \
    } else {                                    \
      foo = typename ## _Manager.head;          \
      typename ## _Manager.head = foo->ar_next; \
    }                                           \
    pthread_mutex_unlock( & typename ## _Manager.lock ); \
    foo->ar_refCnt = 1;                         \
    foo->ar_state = 1;                          \
    return foo;                                 \
  }                                             \
  void typename ## _typeerror( const char* methodname, typename foo ) {                    \
      fprintf(stderr, "Fatal error: called %s on something of type %s\n", methodname, foo->ar_typename); \
      fprintf(stderr, "\tCausing a fatal error so you can see backtrace if running gdb.\n"); \
      fflush(stderr);                                                                      \
      asm("int $0x3\n");                                              \
      int* j = 0;                               \
      int i = *j;                               \
      fprintf(stderr, "%d", i);                 \
  }                                             \
  int typename ## _ref( typename foo ) {        \
    if ( strcmp(foo->ar_typename, #typename ) ) typename ## _typeerror( #typename "_ref", foo); \
    LOCK( &foo->ar_lock );                      \
    if ( !foo->ar_state ) {                     \
      fprintf(stderr, #typename "_ref: Fatal error\n");                                    \
      fprintf(stderr, "\tObject seems to be on free list!\n");                               \
      fprintf(stderr, "\tCurrent reference count = %d\n", foo->ar_refCnt);                   \
      fprintf(stderr, "\tCausing a fatal error so you can see backtrace if running gdb.\n"); \
      fflush(stderr);                                                                      \
      int* j = 0;                               \
      int i = *j;                               \
      fprintf(stderr, "%d", i);                 \
    }                                           \
    int val = ++foo->ar_refCnt;                 \
    UNLOCK( &foo->ar_lock );                    \
    return val;                                 \
  }                                             \
  int typename ## _release( typename foo ) { \
    if ( strcmp(foo->ar_typename, #typename ) ) typename ## _typeerror( #typename "_release", foo); \
    LOCK( &foo->ar_lock );                      \
    if ( !foo->ar_state ) {                     \
      fprintf(stderr, #typename "_release: Fatal error\n");                                \
      fprintf(stderr, "\tObject seems to be on free list!\n");                               \
      fprintf(stderr, "\tCurrent reference count = %d\n", foo->ar_refCnt);                   \
      fprintf(stderr, "\tCausing a fatal error so you can see backtrace if running gdb.\n"); \
      fflush(stderr);                                                                      \
      int* j = 0;                               \
      int i = *j;                               \
      fprintf(stderr, "%d", i);                 \
    }                                           \
    int cnt = --foo->ar_refCnt;                 \
    UNLOCK( &foo->ar_lock );                    \
    if ( cnt == 0 ) {                           \
      if (typename ## _Manager.destructor) typename ## _Manager.destructor(foo); \
      foo->ar_state = 0;                        \
      pthread_mutex_lock( & typename ## _Manager.lock ); \
      foo->ar_next = typename ## _Manager.head; \
      typename ## _Manager.head = foo;          \
      pthread_mutex_unlock( & typename ## _Manager.lock );\
    }                                           \
    return cnt;                                 \
  } 

#endif // FACTORY_H
