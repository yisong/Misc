/*

Given two binary strings, return their sum (also a binary string).

a = 11
b = 1
Return 100

*/

public class Solution {
    /**
     * @param a a number
     * @param b a number
     * @return the result
     */
    public String addBinary(String a, String b) {
        // Write your code here
        
        String result = "";
        
        int incre = 0;
        
        for (int i = 0; i < Math.max(a.length(), b.length()); i++) {
            int c = getNum(a, a.length() - 1 - i) + getNum(b, b.length() - 1 - i) + incre;
            result = c % 2 + result;
            incre = c / 2;
        }
        
        if (incre > 0) {
            result = incre + result;
        }
        
        return result;
    }
    
    private int getNum(String a, int i) {
        return i < 0 ? 0 : a.charAt(i) - '0';
    }
}
