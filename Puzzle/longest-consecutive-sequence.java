/***

Given an unsorted array of integers, find the length of the longest consecutive elements sequence.

For example,
Given [100, 4, 200, 1, 3, 2],
The longest consecutive elements sequence is [1, 2, 3, 4]. Return its length: 4.

Your algorithm should run in O(n) complexity.

***/

public class Solution {
    public int longestConsecutive(int[] num) {
        HashSet<Integer> s = new HashSet<Integer>();
        for (int n : num) {
            s.add(n);
        }
        
        int max = 0;
        for (int n : num) {
            int i = n - 1;
            while(s.contains(i)) {
                s.remove(i);
                i--;
            }
            int j = n + 1;
            while(s.contains(j)) {
                s.remove(j);
                j++;
            }
            max = Math.max(max, j - i - 1);
        }
        return max;
    }
}
