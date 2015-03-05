/***

Follow up for problem "Populating Next Right Pointers in Each Node".

What if the given tree could be any binary tree? Would your previous solution still work?

Note:

You may only use constant extra space.
For example,
Given the following binary tree,
         1
       /  \
      2    3
     / \    \
    4   5    7
After calling your function, the tree should look like:
         1 -> NULL
       /  \
      2 -> 3 -> NULL
     / \    \
    4-> 5 -> 7 -> NULL
    
***/

/**
 * Definition for binary tree with next pointer.
 * public class TreeLinkNode {
 *     int val;
 *     TreeLinkNode left, right, next;
 *     TreeLinkNode(int x) { val = x; }
 * }
 */
public class Solution {
    public void connect(TreeLinkNode root) {
       if (root == null) {
           return;
       }
       root.next = null;
       helper(root);
    }
    
    private void helper(TreeLinkNode n) {
        TreeLinkNode next = helper2(n);
  
        if (n.right != null) {
            n.right.next = next;
            helper(n.right);
        }
        
        if (n.left != null) {
            n.left.next = n.right == null ? next : n.right;
            helper(n.left);
        }
    }
    
    private TreeLinkNode helper2(TreeLinkNode n) {
        if (n.next == null) {
            return null;
        }
        if (n.next.left != null) {
            return n.next.left;
        }
        if (n.next.right != null) {
            return n.next.right;
        }
        return helper2(n.next);
    }
}
    
