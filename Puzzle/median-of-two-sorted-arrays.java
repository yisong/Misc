/***
There are two sorted arrays A and B of size m and n respectively. 
Find the median of the two sorted arrays. 
The overall run time complexity should be O(log (m+n)).
***/

public class Solution {
    public double findMedianSortedArrays(int A[], int B[]) {
        int m = A.length;
        int n = B.length;
        
        int total = m + n;
        
        if (total % 2 == 1) {
            return findKth(A, 0, m, B, 0, n, (total + 1)/2);
        } else {
            return (findKth(A, 0, m, B, 0, n, total / 2) + findKth(A, 0, m, B, 0, n, total / 2 + 1)) / 2.0;
        }
    }
    
    private int findKth(int A[], int a1, int a2, int B[], int b1, int b2, int k) {
        if ((a2 - a1) == 0) {
            return B[b1 + k - 1];
        }
        if ((a2 - a1) > (b2 - b1)) {
            return findKth(B, b1, b2, A, a1, a2, k);
        }
        if (k == 1) {
            return Math.min(A[a1], B[b1]);
        }

        
        
        int deltaA = Math.min(a2 - a1, k / 2);
        int deltaB = k - deltaA;
        
        int posA = a1 + deltaA - 1;
        int posB = b1 + deltaB - 1;
        
        int x = A[posA];
        int y = B[posB];
        
        if (x == y) {
            return x;
        } else if (x > y) {
            return findKth(A, a1, posA + 1, B, posB + 1, b2, k - deltaB);
        } else {
            return findKth(A, posA + 1,  a2, B, b1, posB + 1, k - deltaA);
        }
    }
}
