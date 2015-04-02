/***

Given two sorted integer arrays A and B, merge B into A as one sorted array.

Note:
You may assume that A has enough space (size that is greater or equal to m + n) to hold additional elements from B. 
The number of elements initialized in A and B are m and n respectively.

***/

class Solution {
public:
    void merge(int A[], int m, int B[], int n) {
        int total = m + n - 1;
        int j = m - 1;
        int i = n - 1;
        while(total >= 0) {
            if (j < 0) {
             A[total] = B[i];
                i--;   
            } else if (i < 0 || A[j] >= B[i]) {
                A[total] = A[j];
                j--;
            } else {
                A[total] = B[i];
                i--;
            }
            total--;
        }
    }
};
