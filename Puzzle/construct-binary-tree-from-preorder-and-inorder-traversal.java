/***

Given preorder and inorder traversal of a tree, construct the binary tree.

Note:
You may assume that duplicates do not exist in the tree.

***/

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
      Map<Integer, Integer> m = new HashMap<Integer, Integer>();
      for (int i = 0; i < inorder.length; i++) {
          m.put(inorder[i], i);
      }
      
      return buildTree(preorder, m, 0, preorder.length - 1, 0, preorder.length - 1);
    }
    
    private TreeNode buildTree(int[] a, Map<Integer, Integer> m, int i, int j, int s, int t) {
        if (i > j) {
            return null;
        }
        
        TreeNode root = new TreeNode(a[i]);
        Integer pos = m.get(a[i]);
        
        root.left = buildTree(a, m, i + 1, i + pos - s, s, pos - 1);
        root.right = buildTree(a, m, i + pos - s + 1, j, pos + 1, t);
        
        return root;
    }
}
