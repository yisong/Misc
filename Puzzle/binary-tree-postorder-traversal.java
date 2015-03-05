/***

Given a binary tree, return the postorder traversal of its nodes' values.

For example:
Given binary tree {1,#,2,3},
   1
    \
     2
    /
   3
return [3,2,1].

Note: Recursive solution is trivial, could you do it iteratively?

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
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<Integer>();
        Stack<TreeNode> s = new Stack<TreeNode>();

        TreeNode cur = root;
        while (cur != null) {
            s.push(cur);
            cur = cur.left;
        }
        
        TreeNode prev = null;
        while (!s.isEmpty()) {
            cur = s.pop();
            if (cur.right == null || prev == cur.right) {
                result.add(cur.val);
                prev = cur;
            } else {
                s.push(cur);
                cur = cur.right;
                 while (cur != null) {
                    s.push(cur);
                    cur = cur.left;
                }
            }
        }
        return result;
    }
}
