// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: factorytypeerr.c,v 1.1 2011/10/23 20:10:28 zahorjan Exp $

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "factory.h"

//-----------------------------------------------------------
// This is just a demo program to show what happens if 
// you makea (detectable) type error using factory.h -- e.g.,
// you free an object of class foo using the free method
// for class bar.
//-----------------------------------------------------------

// this would normally go in some .h file
EXPOSETYPE( foo_t )
// this too
EXPOSETYPE( bar_t )

// this goes in each .c file that implements "class" foo_t
INSTANTIATETYPE( foo_t,  char* motto;
                )

// this goes in each .c file that implements "class" bar_t
INSTANTIATETYPE( bar_t,  char* string;
                )

// this goes in EXACTLY ONE .c file, one implementing class foo_t
CREATEMANAGER( foo_t, NULL )

// this goes in EXACTLY ONE .c file, one implementing class bar_t
CREATEMANAGER( bar_t, NULL )


/************************************************
 * main
 ************************************************/

int main(int argc, char* argv[]) {

  foo_t testFoo;

  //-----------------------------------------------------------
  // We create a foo and free it as though it were a bar...
  //-----------------------------------------------------------

  // "malloc()" one
  testFoo = foo_t_acquire();
  if ( !testFoo ) {
    printf("acquire() failed!)\n");
    exit(1);
  }
  testFoo->motto = "I am a foo";

  // Now trigger the error.  Should result in entering 
  // the debugger, assuming you've run this in the debugger.
  bar_t_release( (bar_t)testFoo );


  return 0;
}

