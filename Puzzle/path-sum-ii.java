/***

Given a binary tree and a sum, find all root-to-leaf paths where each path's sum equals the given sum.

For example:
Given the below binary tree and sum = 22,
              5
             / \
            4   8
           /   / \
          11  13  4
         /  \    / \
        7    2  5   1
return
[
   [5,4,11,2],
   [5,8,4,5]
]

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
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        
        if (root == null) {
            return list;
        }
        
        List<Integer> current = new ArrayList<Integer>();
        addPathSum(root, sum, list, current);
        return list;
    }
    
    private void addPathSum(TreeNode root, int sum, List<List<Integer>> list, List<Integer> current) {
        current.add(root.val);
        if (root.left == null && root.right == null) {
            if (root.val == sum) {
                addPath(list, current);
            }
        } else {
            if (root.left != null) {
                addPathSum(root.left, sum - root.val, list, current);
            }
            if (root.right != null) {
                addPathSum(root.right, sum - root.val, list, current);
            }
        }
        current.remove(current.size() - 1);
    }
    
    private void addPath(List<List<Integer>> list, List<Integer> current) {
       List<Integer> match = new ArrayList<Integer>(); 
       for (int i = 0; i < current.size(); i++) {
           match.add(current.get(i));
       }
       list.add(match);
    }
    
    
}
