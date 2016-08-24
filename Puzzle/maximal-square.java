/*

Given a 2D binary matrix filled with 0's and 1's, find the largest square containing all 1's and return its area.

Example
For example, given the following matrix:

1 0 1 0 0
1 0 1 1 1
1 1 1 1 1
1 0 0 1 0
Return 4.

*/

public class Solution {
    /**
     * @param matrix: a matrix of 0 and 1
     * @return: an integer
     */
    public int maxSquare(int[][] matrix) {
        // write your code here
        
        int[][] m = new int[matrix.length][matrix[0].length];
        int max = 0;
        
        
        for (int i = 0; i < matrix.length; i++) {
            m[i][0] = matrix[i][0];
            max = Math.max(m[i][0], max);
        }
        for (int i = 0; i < matrix[0].length; i++) {
            m[0][i] = matrix[0][i];
            max = Math.max(m[0][i], max);
        }
        
        
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    m[i][j] = 0;
                } else {
                    m[i][j] = Math.min(Math.min(m[i - 1][j], m[i][j - 1]), m[i - 1][j - 1]) + 1;
                    max = Math.max(m[i][j], max);
                }
            }
        }
        
        return max * max;
    }
}
