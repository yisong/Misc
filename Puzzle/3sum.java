/***

Given an array S of n integers, are there elements a, b, c in S such that a + b + c = 0? 
Find all unique triplets in the array which gives the sum of zero.

Note:
Elements in a triplet (a,b,c) must be in non-descending order. (ie, a ≤ b ≤ c)
The solution set must not contain duplicate triplets.
    For example, given array S = {-1 0 1 2 -1 -4},

    A solution set is:
    (-1, 0, 1)
    (-1, -1, 2)
    
***/

public class Solution {
    public List<List<Integer>> threeSum(int[] num) {
        
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        Arrays.sort(num);
        
        for (int i = 0; i < num.length - 2; i++) {
            int a = num[i];
            
            if (i > 0 && num[i] == num[i - 1]) {
                continue;
            }
            
            int j = i + 1;
            int k = num.length - 1;
            
            while(j < k) {
                int b = num[j];
                int c = num[k];
                
                if (a + b + c == 0) {
                    j++;
                    k--;
                    if (result.size() > 0) {
                        List<Integer> prev = result.get(result.size() - 1);
                        if (prev.get(0).equals(a) 
                            && prev.get(1).equals(b) 
                            && prev.get(2).equals(c)
                            ) {
                                continue;
                        }
                    }
                    List<Integer> t = new ArrayList<Integer>();
                    t.add(a);
                    t.add(b);
                    t.add(c);
                    result.add(t);
                } else if (a + b + c > 0) {
                    k--;
                } else {
                    j++;
                }
            }
        }
        
        return result;
    }
}
