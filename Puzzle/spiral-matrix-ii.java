/***

Given an integer n, generate a square matrix filled with elements from 1 to n2 in spiral order.

For example,
Given n = 3,

You should return the following matrix:
[
 [ 1, 2, 3 ],
 [ 8, 9, 4 ],
 [ 7, 6, 5 ]
]

***/

public class Solution {
    public int[][] generateMatrix(int n) {
        int[][] m = new int[n][n];
        int x = 1;
        for (int i = 0; i < n / 2; i++) {
            x = addCircle(m, n, i, x);
        }
        if (n % 2 == 1) {
            m[n / 2][n / 2] = n * n;
        }
        return m;
    }
    
    private int addCircle(int[][] m, int n, int c, int x) {
        int l = n - 1 - 2 * c ;
        x = addLine(m, c, c, 0, 1, l, x);
        x = addLine(m, c, n - 1 - c, 1, 0, l, x);
        x = addLine(m, n - 1 - c, n - 1 - c, 0, -1, l, x);
        x = addLine(m, n - 1 - c, c, -1, 0, l, x);
        return x;
    }
    
    private int addLine(int[][] m, int x, int y, int a, int b, int l, int v) {
        for (int i = 0; i < l; i++) {
            m[x][y] = v;
            x += a;
            y += b;
            v++;
        }
        return v;
    }
}
