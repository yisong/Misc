/***

Write a program to solve a Sudoku puzzle by filling the empty cells.

Empty cells are indicated by the character '.'.

You may assume that there will be only one unique solution.

***/

public class Solution {
    public void solveSudoku(char[][] board) {
        solveSudokuHelper(board);
    }
    
    private boolean solveSudokuHelper(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    continue;
                }
                for (char c = '1'; c <= '9'; c++) {
                    if (isValid(board, i, j, c)) {
                        board[i][j] = c;
                        if (solveSudokuHelper(board)) {
                            return true;
                        } 
                    }
                    board[i][j] = '.';
                }
                return false;
            }
        }
        return true;
    }
    
    private boolean isValid(char[][] board, int i, int j, char c) {
        boolean[] m = new boolean[9];
        m[c - '1'] = true;
        for (int s = 0; s < 9; s++) {
            if (board[s][j] == '.') {
                continue;
            }
            
            int v = board[s][j] - '1';
            if (m[v]) {
                return false;
            }
            m[v] = true;
        }
        
        m = new boolean[9];
        m[c - '1'] = true;
        for (int s = 0; s < 9; s++) {
            if (board[i][s] == '.') {
                continue;
            }
            
            int v = board[i][s] - '1';
            if (m[v]) {
                return false;
            }
            m[v] = true;
        }
        
        m = new boolean[9];
        m[c - '1'] = true;
        int u = i / 3;
        int w = j / 3;
        for (int s = 0; s < 9; s++) {
            int x = 3 * u + (s % 3);
            int y = 3 * w + (s / 3);
                   
            if (board[x][y] == '.') {
                continue;
            }
            int v = board[x][y] - '1';
            if (m[v]) {
                return false;
            }
            m[v] = true;
        }
        
        return true;
    }
}
