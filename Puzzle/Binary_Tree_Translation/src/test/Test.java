package test;

import main.Tree;
import main.TreeNode;
import main.TreeTranslation;

public class Test {

	public static void main(String[] args){
		Tree t = new Tree(new TreeNode(new TreeNode(new TreeNode(null, null, 7), new TreeNode(null, null, 8), 2 ), null, 5));
		
		System.out.println(TreeTranslation.treeToString(t));
		
		Tree s = TreeTranslation.stringToTree("/5/2/7/$/$/8/$/$/$");
		System.out.println(s);
	}
}
