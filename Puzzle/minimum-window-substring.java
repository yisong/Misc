/*

Given a string source and a string target, find the minimum window in source which will contain all the characters in target.

If there is no such window in source that covers all characters in target, return the emtpy string "".

If there are multiple such windows, you are guaranteed that there will always be only one unique minimum window in source.

Should the characters in minimum window has the same order in target?

Not necessary.

Example
For source = "ADOBECODEBANC", target = "ABC", the minimum window is "BANC"

*/

public class Solution {
    /**
     * @param source: A string
     * @param target: A string
     * @return: A string denote the minimum window
     *          Return "" if there is no such a string
     */
    public String minWindow(String source, String target) {
        // write your code
        
        Map<Character, Integer> m = new HashMap<Character, Integer>();
        for (char c : target.toCharArray()) {
            Integer i = m.get(c);
            if (i == null) {
                i = 0;
            }
            m.put(c, i + 1);
        }
        
        int i = 0;
        int j = 0;
        int res = source.length() + 1;
        String resS = "";
        int count = m.size();
        
        while(j < source.length()) {
            char c = source.charAt(j);
            if (m.containsKey(c)) {
                Integer n = m.get(c);
                n -= 1;
                m.put(c, n);
                if (n == 0) {
                    count -= 1;
                }
                
                if (count == 0) {
                    while(!m.containsKey(source.charAt(i)) || m.get(source.charAt(i)) != 0) {
                        if (m.containsKey(source.charAt(i))) {
                            m.put(source.charAt(i), m.get(source.charAt(i)) + 1);
                        }
                        i++;
                    }
                    
                    if (res > j - i + 1) {
                        res = j - i + 1;
                        resS = source.substring(i, j + 1);
                    }
                        
                }
            }
            j++;
        }
        
        return resS;
    }
}
