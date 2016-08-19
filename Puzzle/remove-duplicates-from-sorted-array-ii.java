/*

跟进“删除重复数字”：

如果可以允许出现两次重复将如何处理？

样例
给出数组A =[1,1,1,2,2,3]，你的函数应该返回长度5，此时A=[1,1,2,2,3]。

*/

public class Solution {
    /**
     * @param A: a array of integers
     * @return : return an integer
     */
    public int removeDuplicates(int[] nums) {
        // write your code here
        
        if (nums.length == 0) {
            return 0;
        }
        
        int p = 0;
        boolean same = false;
        for (int q = 1; q < nums.length; q++) {
            if (nums[q] != nums[p]) {
                p++;
                nums[p] = nums[q];
                same = false;
            } else {
                if (!same) {
                    p++;
                    nums[p] = nums[q];
                    same = true;
                }
            }
        }
        
        return p + 1;
    }
}
