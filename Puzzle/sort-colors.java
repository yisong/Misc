/*

给定一个包含红，白，蓝且长度为 n 的数组，将数组元素进行分类使相同颜色的元素相邻，并按照红、白、蓝的顺序进行排序。

我们可以使用整数 0，1 和 2 分别代表红，白，蓝。

不能使用代码库中的排序函数来解决这个问题。
排序需要在原数组中进行。

样例
给你数组 [1, 0, 1, 2], 需要将该数组原地排序为 [0, 1, 1, 2]。

*/

class Solution {
    /**
     * @param nums: A list of integer which is 0, 1 or 2 
     * @return: nothing
     */
    public void sortColors(int[] nums) {
        // write your code here
        
        int i = 0;
        int j = nums.length - 1;
        
        while (i < j) {
            if (nums[i] == 0) {
                i++;
            } else if (nums[j] != 0) {
                j--;
            } else {
                nums[j] = nums[i];
                nums[i] = 0;
            }
        }
        
        j = nums.length - 1;
        while (i < j) {
            if (nums[i] == 1) {
                i++;
            } else if (nums[j] != 1) {
                j--;
            } else {
                nums[j] = 2;
                nums[i] = 1;
            }
        }
        
        
        
    }
}
