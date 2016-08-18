/*

假设给出的两个节点都在树中存在

*/

/**
 * Definition of TreeNode:
 * public class TreeNode {
 *     public int val;
 *     public TreeNode left, right;
 *     public TreeNode(int val) {
 *         this.val = val;
 *         this.left = this.right = null;
 *     }
 * }
 */
public class Solution {
    /**
     * @param root: The root of the binary search tree.
     * @param A and B: two nodes in a Binary.
     * @return: Return the least common ancestor(LCA) of the two nodes.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode A, TreeNode B) {
        // write your code here
        
        if (root == null) {
            return null;
        }
        
        if (root == A || root == B) {
            return root;
        }
        
        TreeNode l = root.left != null ? lowestCommonAncestor(root.left, A, B) : null;
        TreeNode r = root.right != null ? lowestCommonAncestor(root.right, A, B) : null;
        
        if (l != null && r != null) {
            return root;
        } else if (l != null) {
            return l;
        } else if (r != null) {
            return r;
        } else {
            return null;
        }
        
    }
}
