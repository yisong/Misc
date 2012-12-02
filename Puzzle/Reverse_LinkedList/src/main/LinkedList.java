package main;

import java.util.Arrays;

public class LinkedList {

	public class Node {
		Node next;
		int value;
		
		public Node(Node n, int v) {
			next = n;
			value = v;
		}
		
		public Node(int[] a, int index){
			if (a.length == index + 1){
				value = a[index];
				next = null;
			} else {
				value = a[index];
				next = new Node (a, index + 1);
			}
		}
		
		public String toString(){
			if (next != null) {
				return value + " -> " + next.toString();
			} else {
				return value + "";
			}
		}
	}
	
	Node front;
	
	public LinkedList (Node n){
		front = n;
	}
	
	public LinkedList (int[] a){
		front = new Node(a, 0);
	}
	
	public String toString(){
		return front.toString();
	}
	
}
