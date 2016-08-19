/*

给出一个二维的字母板和一个单词，寻找字母板网格中是否存在这个单词。

单词可以由按顺序的相邻单元的字母组成，其中相邻单元指的是水平或者垂直方向相邻。每个单元中的字母最多只能使用一次。

样例
给出board =

[
  "ABCE",
  "SFCS",
  "ADEE"
]

word = "ABCCED"， ->返回 true,
word = "SEE"，-> 返回 true,
word = "ABCB"， -> 返回 false.

*/

public class Solution {
    /**
     * @param board: A list of lists of character
     * @param word: A string
     * @return: A boolean
     */
    public boolean exist(char[][] board, String word) {
        // write your code here
        
        boolean[][] r = new boolean[board.length][board[0].length];
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (match(board, word, 0, r, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean match(char[][] board, String w, int n, boolean[][] r, int i, int j) {
        char c = w.charAt(n);
        if (r[i][j] || c != board[i][j]) {
            return false;
        }
        
        r[i][j] = true;
        if (n == w.length() - 1) {
            return true;
        }
        
        for (int[] p : next(i, j, board.length, board[0].length)) {
            if (match(board, w, n + 1, r, p[0], p[1])) {
                return true;
            }
        }
        r[i][j] = false;
        return false;
        
    }
    
    private List<int[]> next(int i, int j, int m, int n) {
        List<int[]> result = new ArrayList<int[]>();
        if (i > 0) {
            result.add(new int[]{i - 1, j});
        } 
        if (j > 0) {
            result.add(new int[]{i, j - 1});    
        }
        if (i < m - 1) {
            result.add(new int[]{i + 1, j});
        }
        if (j < n - 1) {
            result.add(new int[]{i, j + 1});
        }
        return result;
    }
    
}

