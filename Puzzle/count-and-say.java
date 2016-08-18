/*

报数指的是，按照其中的整数的顺序进行报数，然后得到下一个数。如下所示：

1, 11, 21, 1211, 111221, ...

1 读作 "one 1" -> 11.

11 读作 "two 1s" -> 21.

21 读作 "one 2, then one 1" -> 1211.

给定一个整数 n, 返回 第 n 个顺序。

给定 n = 5, 返回 "111221".

*/

public class Solution {
    /**
     * @param n the nth
     * @return the nth sequence
     */
    public String countAndSay(int n) {
        // Write your code here
        String a = "1";
        while (n > 1) {
            a = change(a);
            n--;
        }
        return a;
    }
    
    private String change (String a) {
        StringBuffer sb = new StringBuffer();
        
        int i = 0;
        for (int j = i + 1; j < a.length(); j++) {
            if (a.charAt(j) == a.charAt(i)) {
                continue;
            }
            sb.append(j - i);
            sb.append(a.charAt(i));
            i = j;
        }
        
        sb.append(a.length() - i);
        sb.append(a.charAt(i));
        return sb.toString();
    }
}
