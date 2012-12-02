package main;

public class CommonTreeLCA {

	public static TreeNode compute(TreeNode a, TreeNode b, Tree t){
		return helper(a, b, t.root).n;
	}
	
	private static class Result {
		private int i;
		private TreeNode n;
		
		private Result (int i, TreeNode n) {
			this.i = i;
			this.n = n;
		}
	}
	
	private static Result helper(TreeNode a, TreeNode b, TreeNode tar){
		if (tar == null) {
			return new Result(0, null);
		}
		
		Result rl = helper(a, b, tar.left);
		Result rr = helper(a, b, tar.right);
		
		if (tar == a || tar == b) {
			return new Result(rl.i + rr.i + 1, tar);
		} else if (rl.i == 2) {
			return rl;
		} else if (rr.i == 2) {
			return rr;
		} else {
			return new Result(rl.i + rr.i, tar);
		}
	}
}
