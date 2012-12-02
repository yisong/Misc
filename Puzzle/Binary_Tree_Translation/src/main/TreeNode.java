package main;



public class TreeNode {

		public TreeNode left;
		public TreeNode right;
		public int value;
		
		public TreeNode(TreeNode l, TreeNode r, int v) {
			left = l;
			right = r;
			value = v;
		}
		
		public String toString(){
			String s = value + " (";
			if (left != null){
				s += left.toString();
			}
			
			s += "), (";
			
			if(right != null){
				s += right.toString();
			}
			s +=") ";
			return s;
		}
}

