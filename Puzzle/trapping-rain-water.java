/***
Given n non-negative integers representing an elevation map where the width of each bar is 1, 
compute how much water it is able to trap after raining.

For example, 
Given [0,1,0,2,1,0,1,3,2,1,2,1], return 6.

***/

public class Solution {
    public int trap(int[] A) {
        int result = 0;
        if (A.length == 0) {
            return 0;
        }
        
        int left = A[0];
        int tmp = 0;
        for (int i = 1; i < A.length; i++) {
            if (A[i] >= left) {
                result += tmp;
                tmp = 0;
                left = A[i];
            } else {
                tmp += left - A[i];
            }
        }
        
        int right = A[A.length - 1];
        tmp = 0;
        for (int i = A.length - 2; i>=0; i--) {
            if (A[i] > right) {
                result += tmp;
                tmp = 0;
                right = A[i];
            } else {
                tmp += right -A[i];
            }
        }
        return result;
    }
}
