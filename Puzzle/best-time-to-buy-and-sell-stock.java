/***

Say you have an array for which the ith element is the price of a given stock on day i.

If you were only permitted to complete at most one transaction 
(ie, buy one and sell one share of the stock), 
design an algorithm to find the maximum profit.

***/

public class Solution {
    public int maxProfit(int[] prices) {
      int min = 0;
      int profit = 0;
      
      for (int i = 1; i < prices.length; i++) {
          if (prices[i] >= prices[min]) {
              profit = Math.max(profit, prices[i] - prices[min]);
          } else {
              min = i;
          }
      }
      return profit;
    }
}
