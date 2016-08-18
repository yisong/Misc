/*

给一棵二叉树，找出从根节点到叶子节点的所有路径。


给出下面这棵二叉树：

   1
 /   \
2     3
 \
  5
所有根到叶子的路径为：

[
  "1->2->5",
  "1->3"
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
 
import java.util.*;
 
public class Solution {
    /**
     * @param root the root of the binary tree
     * @return all root-to-leaf paths
     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<String>();
        
        if (root == null) {
            return result;
        }
        
        String pathPrefix = root.val + "";
        
        if (root.left == null && root.right == null) {
            result.add(pathPrefix);
            return result;
        } 
        
        if (root.left != null) {
            addPath(root.left, pathPrefix, result);
        }
        
        if (root.right != null) {
            addPath(root.right, pathPrefix, result);
        }
        
        return result;
    }
    
    private void addPath(TreeNode root, String prefix, List<String> result) {
        prefix = prefix + "->" + root.val;
        
        if (root.left == null && root.right == null) {
            result.add(prefix);
            return;
        }
        
        if (root.left != null) {
            addPath(root.left, prefix, result);
        }
        
        if (root.right != null) {
            addPath(root.right, prefix, result);
        }
        
        return;
        
    }
}
