/***

Given a string, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.

For example,
"A man, a plan, a canal: Panama" is a palindrome.
"race a car" is not a palindrome.

***/

public class Solution {
    public boolean isPalindrome(String s) {
        s = s.toLowerCase();
        if (s.length() == 0) {
            return true;
        }   
    
        int i = 0;
        int j = s.length() - 1;
        
        while (i < j) {
            char ci = s.charAt(i);
            char cj = s.charAt(j);
            if (!((ci <= 'z' && ci >= 'a') || (ci <= '9' && ci >= '0'))) {
                i++;
            } else if (!((cj <= 'z' && cj >= 'a') || (cj <= '9' && cj >= '0'))) {
                j--;
            } else if (ci != cj) {
                return false;
            } else {
                i++;
                j--;
            }
        }
        
        return true;
    }
}
