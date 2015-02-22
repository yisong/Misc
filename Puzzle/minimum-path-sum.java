/***
Given a m x n grid filled with non-negative numbers, 
find a path from top left to bottom right which minimizes the sum of all numbers along its path.

Note: You can only move either down or right at any point in time.
***/

public class Solution {
    public int minPathSum(int[][] grid) {
      int m = grid.length;
      int n = grid[0].length;
      
      int a[] = new int[n];
      a[0] = grid[0][0];
      for (int j = 1; j < n; j ++) {
          a[j] = a[j - 1] + grid[0][j];
      }
      for (int i = 1; i < m; i++) {
          a[0] = a[0] + grid[i][0];
          for (int j = 1; j < n; j++) {
              a[j] = Math.min(a[j - 1], a[j]) + grid[i][j];
          }
      }
      return a[n - 1];
    }
}
