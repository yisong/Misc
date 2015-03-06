/***

Given a collection of integers that might contain duplicates, S, return all possible subsets.

Note:
Elements in a subset must be in non-descending order.
The solution set must not contain duplicate subsets.
For example,
If S = [1,2,2], a solution is:

[
  [2],
  [1],
  [1,2,2],
  [2,2],
  [1,2],
  []
]

***/

public class Solution {
    public List<List<Integer>> subsetsWithDup(int[] num) {
        Arrays.sort(num);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        result.add(new ArrayList<Integer>());
        int same = 0;
        for (int i = 0; i < num.length; i++) {
            int size = result.size();
            if (i > 0 && num[i] == num[i - 1]) {
                same ++;
            } else {
                same = 0;
            }
            for (int j = 0; j < size; j++) {
                List<Integer> prev = result.get(j);
                if (same > 0 && 
                    (prev.size() < same || num[i] != prev.get(prev.size() - same))) {
                    continue;
                }
                ArrayList<Integer> clone = new ArrayList<Integer>();
                for (int k = 0; k < prev.size(); k++) {
                    clone.add(prev.get(k));
                }
                clone.add(num[i]);
                result.add(clone);
            }
        }
        return result;      
    }
}
