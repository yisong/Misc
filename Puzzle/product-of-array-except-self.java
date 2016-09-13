/*

Given an array of n integers where n > 1, nums, return an array output such that output[i] is equal to the product of all the elements of nums except nums[i].

Solve it without division and in O(n).

For example, given [1,2,3,4], return [24,12,8,6].

*/

public class Solution {
    public int[] productExceptSelf(int[] nums) {
        int[] a = new int[nums.length];
        int[] b = new int[nums.length];
        a[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            a[i] = nums[i] * a[i - 1];
        }
        b[nums.length - 1] = nums[nums.length - 1];
        for (int i = nums.length - 2; i >= 0; i--) {
            b[i] = nums[i] * b[i + 1];
        }
        int[] result = new int[nums.length];
        result[0] = b[1];
        result[nums.length - 1] = a[nums.length - 2];
        for (int i = 1; i < nums.length - 1; i++) {
            result[i] = a[i - 1] * b[i + 1];
        }
        return result;
    }
}
