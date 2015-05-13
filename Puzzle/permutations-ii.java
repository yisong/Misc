/***

Given a collection of numbers that might contain duplicates, return all possible unique permutations.

For example,
[1,1,2] have the following unique permutations:
[1,1,2], [1,2,1], and [2,1,1].

***/

public class Solution {
    public List<List<Integer>> permuteUnique(int[] nums) {
        
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
        
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (mark[i]) {
                continue;
            }
            if (set.contains(nums[i])) {
                continue;
            }
            set.add(nums[i]);
            list.add(nums[i]);
            mark[i] = true;
            permuteHelper(nums, mark, list, result);
            mark[i] = false;
            list.remove(list.size() - 1);
        }
        
    }
}
