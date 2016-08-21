/*

给定一个由 n 个正整数组成的数组和一个正整数 s ，请找出该数组中满足其和 ≥ s 的最小长度子数组。如果无解，则返回 -1。

*/

public class Solution {
    /**
     * @param nums: an array of integers
     * @param s: an integer
     * @return: an integer representing the minimum size of subarray
     */
    public int minimumSize(int[] nums, int s) {
        // write your code here
        
        int i = 0;
        int j = 0;
        int sum = 0;
        int res = nums.length + 1;
        
        while(j < nums.length) {
            sum += nums[j];
            if (sum >= s) {
                while (sum - nums[i] >= s) {
                    sum -= nums[i];
                    i++;
                }
                if (j - i + 1 < res) {
                    res = j - i + 1;
                }
            }
            j++;
        }
        
        if (res > nums.length) {
            return -1;
        } else {
            return res;
        }
    }
}
