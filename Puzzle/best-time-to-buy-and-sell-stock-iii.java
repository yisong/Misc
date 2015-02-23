/***

Say you have an array for which the ith element is the price of a given stock on day i.

Design an algorithm to find the maximum profit. You may complete at most two transactions.

Note:
You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).

***/

public class Solution {
    public int maxProfit(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }
        int min = 0;
        int[] a = new int[prices.length];
      
      for (int i = 1; i < prices.length; i++) {
          if (prices[i] >= prices[min]) {
              a[i] = Math.max(a[i - 1], prices[i] - prices[min]);
          } else {
              min = i;
              a[i] = a[i - 1];
          }
      }
      
      int profit = a[prices.length - 1];
      int max = prices.length - 1;
      for (int i = prices.length - 2; i >= 0; i--) {
          if (prices[i] <= prices[max]) {
              profit = Math.max(profit, a[i] + prices[max] - prices[i]);
          } else {
              max = i;
          }
      }
      
      return profit;
    }
}
