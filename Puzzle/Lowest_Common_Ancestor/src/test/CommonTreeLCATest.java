package test;

import main.CommonTreeLCA;
import main.Tree;
import main.TreeNode;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class CommonTreeLCATest {
	
	private Tree t;
	private TreeNode root;
	private TreeNode n1;
	private TreeNode n2;
	private TreeNode n3;
	private TreeNode n4;
	private TreeNode n5;
	private TreeNode n6;
	private TreeNode n7;
	private TreeNode n8;
	private TreeNode n9;
	private TreeNode n10;
	private TreeNode n11;
	private TreeNode n12;
	
	
	
	/*
	 *         		root
	 * 		n1				n2
	 * 	n3		n4		n5		n6
	 * n10		n7 n8		n11		n12
	 *		n9
	 */
	
	@Before
	public void setupBeforeTests() {
		n12 = new TreeNode(null, null, 12);
		n11 = new TreeNode(null, null, 11);
		n9 = new TreeNode(null, null, 9);
		n10 = new TreeNode(null, null, 10);
		n5 = new TreeNode(null, null, 5);
		n8 = new TreeNode(null, null, 8);
		n7 = new TreeNode(n9, null, 7);
		n3 = new TreeNode(n10, null, 3);
		n4 = new TreeNode(n7, n8, 4);
		n1 = new TreeNode(n3, n4, 1);
		n6 = new TreeNode(n11, n12, 6);
		n2 = new TreeNode(n5, n6, 2);
		root = new TreeNode(n1, n2, 0);
		t= new Tree(root);
	}
	
	@Test
	public void test(){
		assertTrue(CommonTreeLCA.compute(n11, n12, t) == n6);
		assertTrue(CommonTreeLCA.compute(n9, n10, t) == n1);
		assertTrue(CommonTreeLCA.compute(n9, n12, t) == root);
		assertTrue(CommonTreeLCA.compute(n5, n2, t) == n2);
		assertTrue(CommonTreeLCA.compute(n5, n8, t) == root);
	}

}
