package test;

import main.LinkedList;
import main.ReverseLinkedList;

public class Test {

	public static void main(String[] args){
		LinkedList l = new LinkedList(new int[]{1, 2, 3, 4, 5});
		ReverseLinkedList.reverseLinkedList(l);
		
		System.out.println(l);
		
		LinkedList n = new LinkedList(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
		ReverseLinkedList.reverseLinkedListByGroup(n, 3);
		
		System.out.println(n);
		
		LinkedList m = new LinkedList(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
		ReverseLinkedList.reverseLinkedListByGroup(m, 3);
		
		System.out.println(m);
		
	}
	
}
