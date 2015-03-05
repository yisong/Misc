/***

Given a triangle, find the minimum path sum from top to bottom. Each step you may move to adjacent numbers on the row below.

For example, given the following triangle
[
     [2],
    [3,4],
   [6,5,7],
  [4,1,8,3]
]
The minimum path sum from top to bottom is 11 (i.e., 2 + 3 + 5 + 1 = 11).

Note:
Bonus point if you are able to do this using only O(n) extra space, where n is the total number of rows in the triangle

***/

public class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
       int[] min = new int[triangle.size()];
       for (int i = 0; i < triangle.size(); i ++) {
           min[i] = triangle.get(triangle.size() - 1).get(i);
       }
       
       for (int i = triangle.size() - 1; i >= 1; i--) {
           for (int j = 0; j < i; j ++) {
               min[j] = triangle.get(i - 1).get(j) + Math.min(min[j], min[j + 1]);
           }
       }
        
       return min[0];
    }
}
