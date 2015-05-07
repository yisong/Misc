/***

Given a binary tree, flatten it to a linked list in-place.

For example,
Given

         1
        / \
       2   5
      / \   \
     3   4   6
The flattened tree should look like:
   1
    \
     2
      \
       3
        \
         4
          \
           5
            \
             6
             
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
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode> s = new Stack<TreeNode>();
        addNode(s, root);
        TreeNode last = root;
        last.left = null;
        last.right = null;
        
        
        while (!s.isEmpty()) {
            TreeNode n = s.pop();
            addNode(s, n);
            last.right = n;
            last = n;
            last.left = null;
            last.right = null;   
        }
        
    }
    
    private void addNode(Stack<TreeNode> s, TreeNode root) {
        if (root.right != null) {
            s.push(root.right);
        }
        
        if (root.left != null) {
            s.push(root.left);
        }
    }
}
