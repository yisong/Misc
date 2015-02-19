/***
Given a binary tree, determine if it is height-balanced.

For this problem, a height-balanced binary tree is defined as 
a binary tree in which the depth of the two subtrees of every node never differ by more than 1.
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
    public boolean isBalanced(TreeNode root) {
      return getHeight(root) >= 0;  
    }
    
    private int getHeight(TreeNode n) {
        if (n == null) {
            return 0;
        }
        
        int lh = getHeight(n.left);
        int rh = getHeight(n.right);
        
        if (lh == -1 || rh == -1 || Math.abs(lh - rh) > 1) {
            return -1;
        }
        
        return Math.max(lh, rh) + 1;
    }
}
