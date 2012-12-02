// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: factorydemo.c,v 1.1 2011/10/23 20:10:28 zahorjan Exp $

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "factory.h"

// this would normally go in some .h file
EXPOSETYPE( factorydemo_t )

// this goes in each .c file that implements "class" factorydemo_t
INSTANTIATETYPE( factorydemo_t,  int dummyInt;
                                 char* name;
                )

// this goes in EXACTLY ONE .c file, one implementing class factorydemo_t
void factorydemo_t_destructor( factorydemo_t foo ) {
  if ( foo->name ) free(foo->name);
}
CREATEMANAGER( factorydemo_t, factorydemo_t_destructor  )

/************************************************
 * main
 ************************************************/

int main(int argc, char* argv[]) {

  // declare a factory test object
  factorydemo_t testObj;

  //-----------------------------------------------------------
  // Example 1: normal usage
  //-----------------------------------------------------------

  // "malloc()" one
  testObj = factorydemo_t_acquire();
  if ( !testObj ) {
    printf("acquire() failed!)\n");
    exit(1);
  }
  // testObj ref count == 1

  // at this point, do the object initialization that normally
  // goes in a constructor.  E.g.,
  testObj->dummyInt = 1;
  testObj->name = strdup("trial 1");  // that just did a malloc()...

  // "free()" the object.
  // Because it's current ref cnt is 1, this call will put it
  // on a free list.  The value returned from this call (ignored in
  // this code) is the decremented reference count (so 0, in this case).
  // We pass a pointer to "destructor" code, because we have to clean up
  // the object in a class-specific way.  If the argument is NULL, or
  // if the decremented reference count isn't 0, the destructor function
  // isn't called.
  factorydemo_t_release( testObj );

  //-----------------------------------------------------------
  // Example 2: duplicating pointers
  //-----------------------------------------------------------

  testObj = factorydemo_t_acquire();
  if ( !testObj ) {
    printf("acquire() failed!)\n");
    exit(1);
  }
  testObj->dummyInt = 2;
  testObj->name = strdup("trial 2");

  // get ready to copy the pointer
  factorydemo_t pointerCopy;

  // first increment the reference count
  factorydemo_t_ref( testObj );  // ref count == 2 
  // then copy
  pointerCopy = testObj;

  // free the original. The destructor won't be called.
  int cnt = factorydemo_t_release( testObj );
  printf("First release: cnt = %d\n", cnt);

  // free the (last remaining) copy.  Destructor will be called.
  cnt = factorydemo_t_release( pointerCopy );
  printf("Second release: cnt = %d\n", cnt);


  //-----------------------------------------------------------
  // Example 3: An error: calling ref() at the wrong time.
  //   This will cause a fatal error, allowing you to re-run
  //   under gdb, then to use the gdb 'bt' command to see
  //   a stack trace
  //-----------------------------------------------------------

  testObj = factorydemo_t_acquire();
  if ( !testObj ) {
    printf("acquire() failed!)\n");
    exit(1);
  }
  testObj->dummyInt = 3;
  testObj->name = NULL;

  // note: forgot to call ref() here...
  pointerCopy = testObj;
  factorydemo_t_release( testObj );

  // but did call it here
  factorydemo_t_ref(pointerCopy);   // ka-boom!
  factorydemo_t_release( pointerCopy );

  printf("All done\n");

  return 0;
}

