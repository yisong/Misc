/***

Determine if a Sudoku is valid, according to: Sudoku Puzzles - The Rules.

The Sudoku board could be partially filled, where empty cells are filled with the character '.'.

A partially filled sudoku which is valid.

Note:
A valid Sudoku board (partially filled) is not necessarily solvable. Only the filled cells need to be validated.

***/

public class Solution {
    public boolean isValidSudoku(char[][] board) {
        
        for (int i = 0; i < 9; i++) {
            boolean[] set = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') {
                    continue;
                }
                int v = board[i][j] - '1';
                if (set[v]) {
                    return false;
                }
                set[v] = true;
            }
        }
        
        for (int i = 0; i < 9; i++) {
            boolean[] set = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (board[j][i] == '.') {
                    continue;
                }
                int v = board[j][i] - '1';
                if (set[v]) {
                    return false;
                }
                set[v] = true;
            }
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
               boolean[] set = new boolean[9];
               for (int k = 0; k < 9; k++) {
                   int x = 3 * i + (k % 3);
                   int y = 3 * j + (k / 3);
                   
                   if (board[x][y] == '.') {
                    continue;
                    }
                    int v = board[x][y] - '1';
                    if (set[v]) {
                        return false;
                    }
                    set[v] = true;
                }
            }
        }
                
        return true;       
    }
}
