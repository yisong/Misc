/***

Implement regular expression matching with support for '.' and '*'.

'.' Matches any single character.
'*' Matches zero or more of the preceding element.

The matching should cover the entire input string (not partial).

The function prototype should be:
bool isMatch(const char *s, const char *p)

Some examples:
isMatch("aa","a") → false
isMatch("aa","aa") → true
isMatch("aaa","aa") → false
isMatch("aa", "a*") → true
isMatch("aa", ".*") → true
isMatch("ab", ".*") → true
isMatch("aab", "c*a*b") → true

***/

public class Solution {
    public boolean isMatch(String s, String p) {
        if (p.length() == 0) {
            return s.length() == 0;
        }
        
        int m = s.length();
        int n = p.length();
        
        boolean[][] a = new boolean[m + 1][n + 1];
        a[0][0] = true;
        for (int j = 0; j < n; j++) {
            char pc = p.charAt(j);
            if (pc != '*') {
                for (int i = 0; i < m; i++) {
                    if (a[i][j] && (pc == s.charAt(i) || pc == '.')) {
                        a[i + 1][j + 1] = true;
                    }
                }
            } else {
                // assert j > 0
                if (a[0][j - 1]) {
                    a[0][j + 1] = true;
                }
                if (p.charAt(j - 1) != '.') {
                    for (int i = 0; i < m; i++) {
                        if (a[i + 1][j - 1] || a[i + 1][j] || 
                            (i > 0 && a[i][j + 1] && s.charAt(i) == s.charAt(i - 1) && s.charAt(i) == p.charAt(j - 1))) {
                                a[i + 1][j + 1] = true;
                            }
                    }
                } else {
                    for (int i = 0; i < m; i++) {
                        if (a[i + 1][j - 1] || a[i + 1][j] || a[i][j + 1]) {
                            a[i + 1][j + 1] = true;
                        }
                    }
                }
            }
        }
         return a[m][n];   
    }
}
