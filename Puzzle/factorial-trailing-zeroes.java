/***

Given an integer n, return the number of trailing zeroes in n!.

Note: Your solution should be in logarithmic time complexity.

***/

public class Solution {
    public int trailingZeroes(int n) {
        int a = n;
        int b = 0;
        while (a >= 5) {
            a = a / 5;
            b += a;
        }
        return b;
    }
}
