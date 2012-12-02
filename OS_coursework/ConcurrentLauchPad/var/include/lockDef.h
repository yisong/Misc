// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: lockDef.h,v 1.1 2011/10/23 20:10:29 zahorjan Exp $

#ifndef LOCKDEF_H
#define LOCKDEF_H

#include <pthread.h>

#ifdef USE_SPINLOCK
  #define LOCK_t         pthread_spinlock_t
  #define LOCK_INIT(x)   pthread_spin_init(x, PTHREAD_PROCESS_SHARED )
  #define LOCK_DESTROY(x) pthread_spin_destroy(x)
  #define LOCK(x)        pthread_spin_lock(x)
  #define UNLOCK(x)      pthread_spin_unlock(x)
#else
  #define LOCK_t         pthread_mutex_t
  #define LOCK_INIT(x)   pthread_mutex_init(x, NULL )
  #define LOCK_DESTROY(x) pthread_mutex_destroy(x)
  #define LOCK(x)        pthread_mutex_lock(x)
  #define UNLOCK(x)      pthread_mutex_unlock(x)
#endif // USE_SPINLOCK


#endif // LOCKDEF_H
