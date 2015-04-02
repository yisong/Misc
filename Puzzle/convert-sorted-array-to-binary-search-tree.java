/***

Given an array where elements are sorted in ascending order, 
convert it to a height balanced BST.

***/

/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Solution {
    public TreeNode sortedArrayToBST(int[] num) {
        return sortedArrayToBST(num, 0, num.length);
    }
    
    private TreeNode sortedArrayToBST(int[] num, int m, int n) {
        int length = n - m;
        if (length <= 0) {
            return null;
        }
        
        int median = m + length / 2;
        TreeNode node = new TreeNode(num[median]);
        node.left = sortedArrayToBST(num, m, median);
        node.right = sortedArrayToBST(num, median + 1, n);
        return node;
    }
}
