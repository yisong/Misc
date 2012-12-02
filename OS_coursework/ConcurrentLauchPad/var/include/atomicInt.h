// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: atomicInt.h,v 1.1 2011/10/23 20:10:29 zahorjan Exp $

#ifndef ATOMICINT_H
#define ATOMICINT_H

/****************************************************
 * thread-safe increment/decrement of an int
 ****************************************************/

#include "factory.h"

EXPOSETYPE(atomic_int_t);


int atomic_int_create( atomic_int_t* a, int initialValue); // returns 0 for success
void atomic_int_destroy( atomic_int_t a );

int atomic_int_getValue( atomic_int_t a);

// these all return the value of the atomic int, with semantics like ++i vs. i++
int atomic_int_preIncrement( atomic_int_t a);
int atomic_int_postIncrement( atomic_int_t a);

int atomic_int_preDecrement( atomic_int_t a);
int atomic_int_postDecrement( atomic_int_t a);

#endif // ATOMICINT_H
