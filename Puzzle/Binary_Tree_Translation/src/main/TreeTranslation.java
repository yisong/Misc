package main;



/*
 * translate a binary tree to a string and translate back
 */

public class TreeTranslation {

	public static String treeToString(Tree t){
		StringBuffer s = new StringBuffer();
		treeNodeTranslation(t.root, s);
		return s.toString();
	}
	
	private static void treeNodeTranslation(TreeNode n, StringBuffer s){
		if (n == null) {
			s.append("/$");
		} else {
			s.append("/" + n.value);
			treeNodeTranslation(n.left, s);
			treeNodeTranslation(n.right, s);
		}
	}
	
	public static Tree stringToTree(String s){
		TreeNode root = stringToTreeHelper(s, 0).n;
		return new Tree(root);
	}
	
	private static Result stringToTreeHelper(String s, int i){
		if (s.charAt(i+1) == '$'){
			return new Result(null, i + 2);
		} else {
			int end = s.indexOf("/", i + 1);
			int v = Integer.valueOf(s.substring(i + 1, end));
			Result rl = stringToTreeHelper(s, end);
			Result rr = stringToTreeHelper(s, rl.i);
			return new Result(new TreeNode(rl.n, rr.n, v), rr.i);
			
		}
	}
	
	static class Result {
		private TreeNode n;
		private int i;
		
		private Result(TreeNode n, int i){
			this.n = n;
			this.i = i;
		}
	}
	
}
