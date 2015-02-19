/***
Given a list of non negative integers, arrange them such that they form the largest number.

For example, given [3, 30, 34, 5, 9], the largest formed number is 9534330.

Note: The result may be very large, so you need to return a string instead of an integer.
***/

public class Solution {
    public String largestNumber(int[] num) {
       String[] strings = new String[num.length];
       for (int i = 0; i < num.length; i++) {
           strings[i] = Integer.toString(num[i]);
       }
       Arrays.sort(
           strings, 
           new Comparator<String>() {
               public int compare(String s1, String s2) {
                   return -(s1 + s2).compareTo(s2 + s1);
                }
            }
       );
            
        if (Integer.parseInt(strings[0]) == 0) {
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();   
    }
}
