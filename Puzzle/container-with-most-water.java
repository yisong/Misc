/***
Given n non-negative integers a1, a2, ..., an, where each represents a point at coordinate (i, ai). 
n vertical lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0). 
Find two lines, which together with x-axis forms a container, such that the container contains the most water.

Note: You may not slant the container.
***/

public class Solution {
    public int maxArea(int[] height) {
        int i = 0;
        int j = height.length - 1;
        int result = getArea(height, 0, j);
        int prevI = i;
        int prevJ = j;
        while (i != j) {
            if (height[i] <= height[j]) {
                i++;
                if (height[i] > height[prevI]) {
                    result = Math.max(result, getArea(height, i, j));
                    prevI = i;
                }
            } else {
                j--;
                if (height[j] > height[prevJ]) {
                    result = Math.max(result, getArea(height, i, j));
                    prevJ = j;
                }
            }
        }
        return result;
    }
    
    private int getArea(int[] height, int i, int j) {
        return Math.min(height[i], height[j]) * (j - i);
    }
}
