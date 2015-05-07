/***

Given two words (beginWord and endWord), and a dictionary, find the length of shortest transformation sequence from beginWord to endWord, such that:

Only one letter can be changed at a time
Each intermediate word must exist in the dictionary
For example,

Given:
start = "hit"
end = "cog"
dict = ["hot","dot","dog","lot","log"]
As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
return its length 5.

Note:
Return 0 if there is no such transformation sequence.
All words have the same length.
All words contain only lowercase alphabetic characters.

***/

public class Solution {
    public int ladderLength(String beginWord, String endWord, Set<String> wordDict) {
        
        Map<String, Integer> m = new HashMap<String, Integer>();
        Queue<String> q = new LinkedList<String>();
        
        m.put(beginWord, 1);
        q.add(beginWord);
        
        while(!q.isEmpty()) {
            String s = q.poll();
            Integer dis = m.get(s);
            
            for (int i = 0; i < s.length(); i++) {
                char[] ca = s.toCharArray();
                
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == s.charAt(i)) {
                        continue;
                    }
                    ca[i] = c;
                    String newS = new String(ca);
                    if (newS.equals(endWord)) {
                        return dis + 1;
                    }
                    
                    if (!wordDict.contains(newS) || m.containsKey(newS)) {
                        continue;
                    }
                    m.put(newS, dis + 1);
                    q.add(newS);
                }
            }
        }
        
        return 0;
        
    }
}
