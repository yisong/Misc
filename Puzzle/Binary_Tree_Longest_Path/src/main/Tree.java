package main;

public class Tree {
	
	public class TreeNode {
		
		public TreeNode left;
		public TreeNode right;
		public int value;
		
		public TreeNode(TreeNode l, TreeNode r, int v) {
			left = l;
			right = r;
			value = v;
		}
	}
	
	
	public TreeNode root;
	
	public Tree(TreeNode r){
		root = r;
	}
}
