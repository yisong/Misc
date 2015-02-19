/***
Follow up for "Unique Paths":

Now consider if some obstacles are added to the grids. How many unique paths would there be?

An obstacle and empty space is marked as 1 and 0 respectively in the grid.

For example,
There is one obstacle in the middle of a 3x3 grid as illustrated below.

[
  [0,0,0],
  [0,1,0],
  [0,0,0]
]
The total number of unique paths is 2.

Note: m and n will be at most 100.
***/

public class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        
        int[][] a = new int[m][n];
        boolean zeroX = false;
        boolean zeroY = false;
        
        for (int i = m - 1; i >= 0; i--) {
            if (obstacleGrid[i][n - 1] == 1) {
                zeroX = true;
            }
            a[i][n - 1] = zeroX ? 0 : 1;
        }
        
        for (int j = n - 1; j >= 0; j--) {
            if (obstacleGrid[m - 1][j] == 1) {
                zeroY = true;
            }
            a[m - 1][j] = zeroY ? 0 : 1;
        }
        
        for (int i = m - 2; i >= 0; i--) {
            for (int j = n - 2; j >= 0; j--) {
                a[i][j] = obstacleGrid[i][j] == 1 ? 0 : a[i][j + 1] + a[i + 1][j];
            }
        }
        
        return a[0][0];
    }
}

public class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        if (obstacleGrid[m - 1][n - 1] == 1) {
                return 0;
        }
        
        int[] a = new int[n];
        boolean zeroX = false;
        boolean zeroY = false;
        
        a[n - 1] = 1;

        for (int j = n - 2; j >= 0; j--) {
            if (obstacleGrid[m - 1][j] == 1) {
                zeroY = true;
            }
            a[j] = zeroY ? 0 : 1;
        }
        
        for (int i = m - 2; i >= 0; i--) {
            if (obstacleGrid[i][n - 1] == 1) {
                zeroX = true;
            }
            a[n - 1] = zeroX ? 0 : 1;
            for (int j = n - 2; j >= 0; j--) {
                a[j] = obstacleGrid[i][j] == 1 ? 0 : a[j + 1] + a[j];
            }
        }
        
        return a[0];
    }
}
