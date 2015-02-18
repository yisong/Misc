/***
Given a string, find the length of the longest substring without repeating characters. 
For example, the longest substring without repeating letters for "abcabcbb" is "abc", which the length is 3. 
For "bbbbb" the longest substring is "b", with the length of 1.
***/

public class Solution {
    public int lengthOfLongestSubstring(String s) {
       int result = 0;
       int prev = -1;
       Map<Character, Integer> map = new HashMap<Character, Integer>();
       for (int i = 0; i < s.length(); i++) {
           char c = s.charAt(i);
           Integer v = map.get(c);
           if (v != null && prev < v) {
               prev = v;
           }
           result = Math.max(result, i - prev);
           map.put(c, i);
       }
       return result;
    }
}
