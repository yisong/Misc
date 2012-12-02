// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-

#include <stdlib.h>
#include "hashTable.h"

typedef struct hash_tuple_t {
  void* k;
  void* v;
} *hash_tuple_t;

typedef hash_tuple_t *entry_table;

INSTANTIATETYPE( hashTable_t , hash_fn_t hash; hash_key_cmp_fn_t cmp; hash_key_destroy_fn_t keyDes;
                 hash_value_destroy_fn_t valDes; entry_table entryTable; int arraySize; int currentSize;)

static const int DEFAULT_TABLE_SIZE = 7;

static int hashTable_allocateArray(hashTable_t h);
static int hashTable_findPos(hashTable_t h, void* k);
static int hashTable_rehash(hashTable_t h);
static int nextPrime(int n);
static int isPrime (int n);
static int hashTable_addTuple( hashTable_t h, hash_tuple_t tuple);

hashTable_t hashTable_create (int size,
                              hash_fn_t hashFunc,
                              hash_key_cmp_fn_t keyCmpFunc,
                              hash_key_destroy_fn_t destroyKeyFunc,
                              hash_value_destroy_fn_t destroyValFunc ) {

  hashTable_t h = hashTable_t_acquire();
  if ( !h ) return NULL;
  h -> arraySize = DEFAULT_TABLE_SIZE;
  h -> currentSize = 0;
  h -> hash = hashFunc;
  h -> cmp = keyCmpFunc;
  h -> keyDes = destroyKeyFunc;
  h -> valDes = destroyValFunc;
  if ( !hashTable_allocateArray(h) ) {
    free(h);
    return NULL;
  }
  return h;
}

static void hashTable_internal_destroy (hashTable_t t ) {
  int i;
  for( i = 0; i < (t -> arraySize); i++ ) {
    hash_tuple_t tuple = (t -> entryTable)[i];
    if (tuple) {
      if (t -> keyDes) (t -> keyDes)(tuple -> k);
      if (t -> valDes) (t -> valDes)(tuple -> v);
      free(tuple);
    }
  }

  free(t -> entryTable);

}

int hashTable_destroy (hashTable_t t) {
  hashTable_t_release(t);
  return 0;
}

CREATEMANAGER(hashTable_t , hashTable_internal_destroy )

static int hashTable_allocateArray (hashTable_t h) {
  entry_table p = (entry_table)malloc(sizeof(hash_tuple_t) * (h -> arraySize));
  if ( !p ) return 0;
  h -> entryTable = p;
  h -> currentSize = 0;
  int i;
  for (i = 0; i < (h -> arraySize); i++ ) {
    p[i] = NULL;
  }
  return 1;
}

static int hashTable_findPos (hashTable_t h, void* k) {
  int offset = 1;
  int size = h->arraySize;
  hash_fn_t hash = h -> hash;
  hash_key_cmp_fn_t cmp = h -> cmp;
  entry_table p = h -> entryTable;
  int hashValue = hash(k);
  int currentPos = hashValue % size;
  if (currentPos < 0 ) {
    currentPos += size;
  }

  while ( p[currentPos] != NULL && cmp(p[currentPos] -> k, k) != 0) {
    currentPos += offset;
    offset += 2;
    if (currentPos >= size) {
      currentPos -= size;
    }
  }

  return currentPos;
}

void* hashTable_lookupItem(hashTable_t t, void* key) {
  int currentPos = hashTable_findPos(t, key);
  if ((t -> entryTable)[currentPos] != NULL) {
    return (t -> entryTable)[currentPos] -> v;
  } else {
    return NULL;
    }
}

int hashTable_addItem( hashTable_t t, void* key, void* value ) {
  hash_tuple_t tuple = (hash_tuple_t)malloc(sizeof(struct hash_tuple_t));
  if (tuple == NULL ) {
    return 1;
  }

  tuple -> k = key;
  tuple -> v = value;

  return hashTable_addTuple(t, tuple);
}

static int hashTable_addTuple( hashTable_t h, hash_tuple_t tuple) {
  int currentPos = hashTable_findPos(h, tuple -> k);
  if ((h -> entryTable)[currentPos] == NULL ) {
    h -> currentSize ++;
  } else {
    hash_tuple_t r = h -> entryTable[currentPos];
    if (h -> keyDes) (h -> keyDes)(r -> k);
    if (h -> valDes) (h -> valDes)(r -> v);
    free(r);
  }

  h -> entryTable[currentPos] = tuple;
  
  if (h -> currentSize > h -> arraySize / 2) {
    return hashTable_rehash(h);
  }

  return 0;
}

static int hashTable_rehash(hashTable_t h){
  int oldSize = h->arraySize;
  entry_table oldTable = h->entryTable;
  h->arraySize = nextPrime(2*oldSize);
  if ( !hashTable_allocateArray(h)) return 1;

  int i;
  for ( i = 0; i < oldSize; i++) {
    if ( oldTable[i] != NULL ) {
      hashTable_addTuple(h, oldTable[i]);
    }
  }

  free(oldTable);
  return 0;
}

static int nextPrime(int n) {
  int i = 1;
  while (!isPrime(n + i)) {
    i += 2;
  }

  return n + i;
}

static int isPrime(int n) {
  if (n == 1) return 0;

  int i;
  for (i = 3; i < n; i +=2) {
    if (n % i == 0) {
      return 0;
    }
  }

  return 1;
}  
  
  
