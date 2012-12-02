#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "hashTable.h"

static int hashFn (void* k) { return 0; }
static int cmpFn (void* k1, void* k2) {return *(int *)k1 - *(int *)k2;}
static void freeFn (void* v) { free(v); }

int main (int argc, char* argv[]) {
  hashTable_t h = hashTable_create(29, hashFn, cmpFn, freeFn, freeFn);

  int i;
  printf("Phase1: add item\n");
  for (i = 0; i < 50; i ++ ) {
    int* v = malloc(sizeof(int));
    *v = i * i ;
    int* k = malloc(sizeof(int));
    *k = i;
    int res = hashTable_addItem(h, k, v);
    if (!res) {
      printf("Inserted value %d with key %d\n", *v, *k);
    } else {
      printf("Insertion for value %d with key %d failed\n", *v, *k);
    }
  }

  printf("\nPhase2: lookup item\n");
  for (i = 0; i < 50; i ++) {
    void* v = hashTable_lookupItem(h, &i);
    if (v == NULL ) {
      printf("Not found value with key %d\n", i);
    } else {
      printf("Found value %d with key %d\n", *(int *)v, i);
    }
  }

  printf("\nPhase3: replace item\n");
  for (i = 0; i < 50; i ++ ) {
    int* v = malloc(sizeof(int));
    *v = i * i * i ;
    int* k = malloc(sizeof(int));
    *k = i;
    int res = hashTable_addItem(h, k, v);
    if (!res) {
      printf("Inserted value %d with key %d\n", *v, *k);
    } else {
      printf("Insertion for value %d with key %d failed\n", *v, *k);
    }
  }

  printf("\nPhase4: relookup item\n");
  for (i = 0; i < 50; i ++) {
    void* v = hashTable_lookupItem(h, &i);
    if (v == NULL ) {
      printf("Not found value with key %d\n", i);
    } else {
      printf("Found value %d with key %d\n", *(int *)v, i);
    }
  } 

  hashTable_destroy(h);

  return 0;
}
  
