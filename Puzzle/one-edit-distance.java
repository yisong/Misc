/*

Given two strings S and T, determine if they are both one edit distance apart.

*/

public class Solution {
    public boolean isOneEditDistance(String s, String t) {
        if (s.equals(t)) {
            return false;
        }
        if (Math.abs(s.length() - t.length()) > 1) {
            return false;
        }
        if (s.length() == t.length()) {
            int c = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != t.charAt(i)) {
                    c++;
                    if (c > 1) {
                        return false;
                    }
                }
            }
            return true;
        }
        if (s.length() < t.length()) {
            String tmp = s;
            s = t;
            t = tmp;
        }
        for (int i = 0; i < s.length(); i++) {
            if (t.equals(s.substring(0, i) + s.substring(i + 1, s.length()))) {
                return true;
            }
        }
        return false;
    }
}

