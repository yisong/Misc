/*

Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.

For example, given n = 3, a solution set is:

[
  "((()))",
  "(()())",
  "(())()",
  "()(())",
  "()()()"
]

*/

public class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<String>();
        helper("", 0, 0, n, result);
        return result;
    }
    
    private void helper(String s, int a, int b, int n, List<String> l) {
        if (a == b && b == n) {
            l.add(s);
        }
        else if (a == b) {
            helper(s + '(', a + 1, b, n, l);
        }
        else if (a == n) {
            helper(s + ')', a, b + 1, n, l);
        }
        else {
            helper(s + '(', a + 1, b, n, l);
            helper(s + ')', a, b + 1, n, l);
        }
    }
}
