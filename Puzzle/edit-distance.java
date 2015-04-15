/***

Given two words word1 and word2, find the minimum number of steps required to convert word1 to word2. (each operation is counted as 1 step.)

You have the following 3 operations permitted on a word:

a) Insert a character
b) Delete a character
c) Replace a character

***/

public class Solution {
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
    
        int[][] map = new int[m + 1][n + 1];
        for (int i = 0; i < m + 1; i++) {
            map[i][0] = i;
        }
        for (int j = 0; j < n + 1; j++) {
            map[0][j] = j;
        }
        for (int i = 1; i < m + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                map[i][j] = word1.charAt(i - 1) == word2.charAt(j - 1) ?
                            map[i - 1][j - 1] :
                            Math.min(Math.min(map[i][j - 1], map[i - 1][j]), map[i - 1][j - 1]) + 1; 
            }
        }
        return map[m][n];
    }
}
