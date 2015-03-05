/***
Given numRows, generate the first numRows of Pascal's triangle.

For example, given numRows = 5,
Return

[
     [1],
    [1,1],
   [1,2,1],
  [1,3,3,1],
 [1,4,6,4,1]
]
***/

public class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (numRows == 0) {
            return result;
        } 
        List<Integer> cur = new ArrayList<Integer>();
        cur.add(1);
        result.add(cur);
        if (numRows == 1) {
            return result;
        }
        List<Integer> prev;
        
        for (int i = 1; i < numRows; i++) {
            prev = cur;
            cur = new ArrayList<Integer>();
            cur.add(1);
            for (int j = 1; j < i; j++) {
                cur.add(prev.get(j - 1) + prev.get(j));
            }
            cur.add(1);
            result.add(cur);
        }
        return result;
    }
}
