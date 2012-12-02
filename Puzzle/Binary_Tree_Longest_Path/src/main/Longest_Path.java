package main;

/*
 * find the longest path in the given tree
 */

import main.Tree.TreeNode;

public class Longest_Path {

	private static class Data {
		private int height;
		private int path;
		
		private Data (int h, int p){
			height = h;
			path = p;
		}
	}
	
	public static int longestPath(Tree t){
		return helper(t.root).path;
	}
	
	private static Data helper (TreeNode n) {
		if (n == null) {
			return new Data(0, 0);
		} else {
			Data l = helper(n.left);
			Data r = helper(n.right);
			int path = l.height + r.height;
			int height = Math.max(l.height, r.height) + 1;
			return new Data(height, Math.max(Math.max(path, l.path), r.path));
		}
	}
	
	
	
	
	

}
