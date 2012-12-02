// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: hashTable.h,v 1.2 2011/10/24 04:22:13 zahorjan Exp $

#ifndef HASHTABLE_H
#define HASHTABLE_H

/************************************************************
 * Hash table implementation.
 * Doesn't have to be thread safe.
 * Doesn't have to be dynamically resized.
 ************************************************************/

#include "factory.h"

EXPOSETYPE(hashTable_t)

typedef int (*hash_fn_t)(void* key);                       // a client-supplied hash function: key -> int
typedef int (*hash_key_cmp_fn_t)(void* keyA, void* keyB);  // returns 0 if the keys are equal, not-zero otherwise
typedef void (*hash_value_destroy_fn_t)(void* value);      // callback to client code to destroy a value (e.g., if the hash table
                                                           //   isn't empty when it's destroyed)
typedef void (*hash_key_destroy_fn_t)(void* key);          // callback to destroy a key

extern hashTable_t hashTable_create( int                     size,         // table size
                                     hash_fn_t               hashFunc,     // client's hash function
                                     hash_key_cmp_fn_t       keyCmpFunc,   // client's key comparison function
                                     hash_key_destroy_fn_t   destroyKeyFunc,
                                     hash_value_destroy_fn_t destroyValFunc
                                   );

// well... destroys the hash table
extern int hashTable_destroy( hashTable_t t );

// 0 for success, non-zero for failure
extern int hashTable_addItem( hashTable_t t,
                              void* key,
                              void* value
                            );

// returns the value, or null if key isn't in table
extern void* hashTable_lookupItem(hashTable_t t, void* key);

// to minimize the amount of garden variety code you need to write,
// removeHashItem() is not spec'ed (because it's not needed for this app)
// extern int removeHashItem( hashTable_t, 
//                            void* key,
//                            hash_key_destroy_fn_t destroyKeyFunc,
//                            hash_value_destroy_fn_t destroyValFunc
//                          );
// (So, no need to implement it.)

#endif // HASHTABLE_H
