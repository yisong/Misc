/***

Given a string s and a dictionary of words dict, 
determine if s can be segmented into a space-separated sequence of one or more dictionary words.

For example, given
s = "leetcode",
dict = ["leet", "code"].

Return true because "leetcode" can be segmented as "leet code".

***/

public class Solution {
    public boolean wordBreak(String s, Set<String> wordDict) {
      boolean[] a = new boolean[s.length() + 1];
      a[0] = true;
      
      for (int i = 1; i <= s.length(); i++) {
          a[i] = false;
          for (String word : wordDict) {
              if (word.length() > i) {
                  continue;
              }
              if (a[i - word.length()] &&
                  s.substring(i - word.length(), i).equals(word)) {
                      a[i] = true;
                      break;
                  }
          }
      }
      return a[s.length()];
    }
}
