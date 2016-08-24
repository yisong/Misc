/*

There are a row of n houses, each house can be painted with one of the k colors. 
The cost of painting each house with a certain color is different. You have to paint all the houses such that no two adjacent houses have the same color.

The cost of painting each house with a certain color is represented by a n x k cost matrix. 
For example, costs[0][0] is the cost of painting house 0 with color 0; 
costs[1][2] is the cost of painting house 1 with color 2, and so on... Find the minimum cost to paint all houses.

 Notice

All costs are positive integers.

Example
Given n = 3, k = 3, costs = [[14,2,11],[11,14,5],[14,3,10]] return 10

house 0 is color 2, house 1 is color 3, house 2 is color 2, 2 + 5 + 3 = 10

*/

public class Solution {
    /**
     * @param costs n x k cost matrix
     * @return an integer, the minimum cost to paint all houses
     */
    public int minCostII(int[][] costs) {
        // Write your code here
        
        if (costs.length == 0) {
            return 0;
        }
        
        int n = costs.length;
        int k = costs[0].length;
        
        int prevMin = 0;
        int prev2Min = 0;
        int prevI = -1;
        
        for (int i = 0; i < n; i++) {
            int min = Integer.MAX_VALUE;
            int c2min = min;
            int minI = -1;
            for (int j = 0; j < k; j++) {
                int c = costs[i][j] + (j == prevI ? prev2Min : prevMin);
                if (c <= min) {
                    c2min = min;
                    min = c;
                    minI = j;
                } else if (c < c2min) {
                    c2min = c;
                }
            }
            
            prevMin = min;
            prev2Min = c2min;
            prevI = minI;
        }
        
        return prevMin;
    }
}
