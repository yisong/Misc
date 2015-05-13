/***

Given an array of strings, return all groups of strings that are anagrams.

Note: All inputs will be in lower-case.

***/

public class Solution {
    public List<String> anagrams(String[] strs) {
        HashMap<HashMap<Character, Integer>, ArrayList<String>> superMap = 
            new HashMap<HashMap<Character, Integer>, ArrayList<String>>();
        
        for (String s : strs) {
            HashMap<Character, Integer> map = new HashMap<Character, Integer>();
            for (char c : s.toCharArray()) {
                Integer i = map.get(c);
                if (i != null) {
                    map.put(c, i + 1);
                } else {
                    map.put(c, 1);
                }
            }
            ArrayList<String> list = superMap.get(map);
            if (list == null) {
                list = new ArrayList<String>();
            }
            list.add(s);
            superMap.put(map, list);
        }
        
        ArrayList<String> result = new ArrayList<String>();
        for (Map.Entry<HashMap<Character, Integer>, ArrayList<String>> entry : superMap.entrySet()) {
           if (entry.getValue().size() > 1) {
               result.addAll(entry.getValue());
           } 
        }
        
        return result;
    }
}
