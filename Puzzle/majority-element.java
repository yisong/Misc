/***
Given an array of size n, find the majority element. 
The majority element is the element that appears more than ⌊ n/2 ⌋ times.

You may assume that the array is non-empty and the majority element always exist in the array.
***/

public class Solution {
    public int majorityElement(int[] num) {
        int x = num[0];
        int count = 1;
        for (int i = 1; i < num.length; i++) {
            if (x != num[i]) {
                count--;
            } else {
                count++;
            }
            if (count == 0) {
                x = num[i];
                count = 1;
            }
        }
        return x;
    }
}
