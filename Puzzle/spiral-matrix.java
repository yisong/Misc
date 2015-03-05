/***

Given a matrix of m x n elements (m rows, n columns), return all elements of the matrix in spiral order.

For example,
Given the following matrix:

[
 [ 1, 2, 3 ],
 [ 4, 5, 6 ],
 [ 7, 8, 9 ]
]
You should return [1,2,3,6,9,8,7,4,5].

***/

public class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
       List<Integer> result = new ArrayList<Integer>();
       if (matrix.length == 0 || matrix[0].length == 0) {
           return result;
       }
       int c = Math.min(matrix[0].length, matrix.length);
       for (int i = 0; i < c / 2; i++) {
           addCircle(matrix, result, i);
       }
       
       if (c % 2 == 1) {
           if (matrix.length >= matrix[0].length) {
               addLine(matrix, result, c / 2, c / 2, 1, 0, matrix.length - c + 1);
           } else {
               addLine(matrix, result, c / 2, c / 2, 0, 1, matrix[0].length - c + 1);
           }
       }
       
       return result;
    }
    
    private void addLine(int[][] matrix, List<Integer> result, int x, int y, int a, int b, int c) {
        for (int i = 0; i < c; i++) {
            result.add(matrix[x][y]);
            x += a;
            y += b;    
        }
    }
    
    private void addCircle(int[][] matrix, List<Integer> result, int l) {
        addLine(matrix, result, l, l, 0, 1, matrix[0].length - 1 - 2 * l);
        addLine(matrix, result, l, matrix[0].length - 1 - l, 1, 0, matrix.length - 1 - 2 * l);
        addLine(matrix, result, matrix.length - 1 - l, matrix[0].length - 1 - l, 0, -1, matrix[0].length - 1 - 2 * l);
        addLine(matrix, result, matrix.length - 1 - l, l, -1, 0, matrix.length - 1 - 2 * l);
    }
}
