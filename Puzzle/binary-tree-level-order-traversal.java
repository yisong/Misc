/*

给出一棵二叉树，返回其节点值的层次遍历（逐层从左往右访问)

样例
给一棵二叉树 {3,9,20,#,#,15,7} ：

  3
 / \
9  20
  /  \
 15   7
返回他的分层遍历结果：

[
  [3],
  [9,20],
  [15,7]
]

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
     * @param root: The root of binary tree.
     * @return: Level order a list of lists of integer
     */
    public ArrayList<ArrayList<Integer>> levelOrder(TreeNode root) {
        
        Queue<TreeNode> q = new LinkedList<TreeNode>();
        ArrayList<ArrayList<Integer>> r = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        
        if (root == null) {
            return r;
        }
        
        q.add(root);
        q.add(null);
        while(!q.isEmpty()) {
            TreeNode n = q.remove();
            if (n == null) {
                r.add(tmp);
                tmp = new ArrayList<Integer>();
                if (!q.isEmpty()) {
                    q.add(null);
                }
            } else {
                tmp.add(n.val);
                if (n.left != null) {
                    q.add(n.left);
                }
                if (n.right != null) {
                    q.add(n.right);
                }
            }
            
        }
        
        return r;
        
    }
}
