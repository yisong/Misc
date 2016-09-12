/*

You are a product manager and currently leading a team to develop a new product. Unfortunately, the latest version of your product fails the quality check. Since each version is developed based on the previous version, all the versions after a bad version are also bad.

Suppose you have n versions [1, 2, ..., n] and you want to find out the first bad one, which causes all the following ones to be bad.

You are given an API bool isBadVersion(version) which will return whether version is bad. Implement a function to find the first bad version. You should minimize the number of calls to the API.

*/

/* The isBadVersion API is defined in the parent class VersionControl.
      boolean isBadVersion(int version); */

public class Solution extends VersionControl {
    public int firstBadVersion(int n) {
    // write your code here
        int a = 1;
        int b = n;
        
        Map<Integer, Boolean> m = new HashMap<Integer, Boolean>();
        
        while (true) {
            int c = a + (b - a) / 2;
            if (get(m, c) && (c == 1 || !get(m, c - 1))) {
                return c;
            } else if (get(m, c)) {
                b = c - 1;
            } else {
                a = c + 1;
            }
        }
        
    }
    
    private boolean get(Map<Integer, Boolean> m, int c) {
        Boolean b = m.get(c);
        if (b == null) {
            b = isBadVersion(c);
            m.put(c, b);
        }
        return b;
    }
}
