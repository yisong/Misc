package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * a naive implementation of suffix tree
 * the run time to build a tree is O(n^2)
 * there exists a O(n) algorithm
 */

public class SuffixTree {

	public class SuffixTreeNode{
		
		char value;
		ArrayList<Integer> indexes;
		Map<Character, SuffixTreeNode> children;
		
		public SuffixTreeNode(){
			indexes = new ArrayList<Integer>();
			children = new HashMap<Character, SuffixTreeNode>();
		}
		
		public void addString(String s, int i){

			if(s != null && s.length() > 0){
				char c = s.charAt(0);
				SuffixTreeNode child;
				if(children.containsKey(c)){
					child = children.get(c);
				}else{
					child = new SuffixTreeNode();
					child.value = c;
					children.put(c, child);
				}
				child.indexes.add(i);
				child.addString(s.substring(1), i);
			}
		}
		
		public ArrayList<Integer> find(String s){
			if (s.isEmpty()) {
				return indexes;
			} 
			
			char c = s.charAt(0);
			if (!children.containsKey(c)) {
				return null;
			}
			
			return children.get(c).find(s.substring(1));
			
		}
	}
	
	public SuffixTreeNode root;
	
	public SuffixTree(String s){
		root = new SuffixTreeNode();
		addString(s);
	}
	
	public void addString(String s){
		for(int i = 0; i < s.length(); i ++){
			root.addString(s.substring(i), i);
		}
	}
	
	public ArrayList<Integer> find (String s){
		return root.find(s);
	}
	
}
