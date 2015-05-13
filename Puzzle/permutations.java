/***

Given a collection of numbers, return all possible permutations.

For example,
[1,2,3] have the following permutations:
[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], and [3,2,1].

***/

public class Solution {
    public List<List<Integer>> permute(int[] nums) {
        
        boolean[] mark = new boolean[nums.length];
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        permuteHelper(nums, mark, new ArrayList<Integer>(), result);
        return result;
    }
    
    public void permuteHelper(int[] nums, boolean[] mark, List<Integer> list, List<List<Integer>> result) {
        if (list.size() == nums.length) {
            result.add(new ArrayList<Integer>(list));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (mark[i]) {
                continue;
            }
            list.add(nums[i]);
            mark[i] = true;
            permuteHelper(nums, mark, list, result);
            mark[i] = false;
            list.remove(list.size() - 1);
        }
        
    }
}
