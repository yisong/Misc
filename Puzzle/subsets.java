/***

Given a set of distinct integers, S, return all possible subsets.

Note:
Elements in a subset must be in non-descending order.
The solution set must not contain duplicate subsets.
For example,
If S = [1,2,3], a solution is:

[
  [3],
  [1],
  [2],
  [1,2,3],
  [1,3],
  [2,3],
  [1,2],
  []
]

***/

public class Solution {
    public List<List<Integer>> subsets(int[] S) {
        Arrays.sort(S);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        result.add(new ArrayList<Integer>());
        for (int i = 0; i < S.length; i++) {
            int size = result.size();
            for (int j = 0; j < size; j++) {
                List<Integer> prev = result.get(j);
                ArrayList<Integer> clone = new ArrayList<Integer>();
                for (int k = 0; k < prev.size(); k++) {
                    clone.add(prev.get(k));
                }
                clone.add(S[i]);
                result.add(clone);
            }
        }
        return result;
    }
}
