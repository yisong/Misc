/***
Implement an iterator over a binary search tree (BST). 
Your iterator will be initialized with the root node of a BST.

Calling next() will return the next smallest number in the BST.

Note: next() and hasNext() should run in average O(1) time and uses O(h) memory, 
where h is the height of the tree.
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

public class BSTIterator {

    Stack<TreeNode> s;

    public BSTIterator(TreeNode root) {
        s = new Stack<TreeNode>();
        addNode(root);
    }

    private void addNode(TreeNode root) {
        TreeNode n = root;
        while(n != null) {
            s.push(n);
            n = n.left;
        }
    }

    /** @return whether we have a next smallest number */
    public boolean hasNext() {
        return s.size() > 0;
    }

    /** @return the next smallest number */
    public int next() {
        TreeNode n = s.pop();
        addNode(n.right);
        return n.val;
    }
}

/**
 * Your BSTIterator will be called like this:
 * BSTIterator i = new BSTIterator(root);
 * while (i.hasNext()) v[f()] = i.next();
 */
