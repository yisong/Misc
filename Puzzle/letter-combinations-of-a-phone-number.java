/***

Given a digit string, return all possible letter combinations that the number could represent.

A mapping of digit to letters (just like on the telephone buttons) is given below.

Input:Digit string "23"
Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].

***/

public class Solution {
    
    public List<String> letterCombinations(String digits) {
        if (digits.length() == 0) {
            return new ArrayList<String>();
        }
        
        String[] map = {"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        
        List<String> tmp = new ArrayList<String>();
        tmp.add("");
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < digits.length(); i++) {
            String candidates = map[digits.charAt(i) - '2'];
            for (int j = 0; j < candidates.length(); j++) {
                char c = candidates.charAt(j);
                for (String s : tmp) {
                    result.add(s + c);
                }
            }
            tmp = result;
            result = new ArrayList<String>();
        }
        return tmp;
    }
}
