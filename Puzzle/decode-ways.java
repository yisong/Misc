/*

A message containing letters from A-Z is being encoded to numbers using the following mapping:

'A' -> 1
'B' -> 2
...
'Z' -> 26
Given an encoded message containing digits, determine the total number of ways to decode it.

For example,
Given encoded message "12", it could be decoded as "AB" (1 2) or "L" (12).

The number of ways decoding "12" is 2.

*/

public class Solution {
    public int numDecodings(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        int[] count = new int[s.length() + 1];
        count[s.length()] = 1;
        for (int i = s.length() - 1; i>=0; i--) {
            int c = s.charAt(i) - '0';
            int tmp = 0;
            if (c > 0) {
                tmp += count[i + 1];
            }
            if (c == 1 && i < s.length() - 1) {
                tmp += count[i + 2];
            } else if (c == 2 && i < s.length() - 1 && s.charAt(i + 1) - '0' <= 6) {
                tmp += count[i + 2];
            }
            count[i] = tmp;
        }
        return count[0];
    }
}
