package main;

import main.LinkedList.Node;



/*
 * Reverse LinkList by group
 */

public class ReverseLinkedList {
	
	
	public static void reverseLinkedList(LinkedList l){
		Node before = null;
		Node current = l.front;

		while(current != null){
			Node n = current.next;
			current.next=before;
			before = current;
			current = n;
		}
		
		l.front = before;
	}
	
	public static void reverseLinkedListByGroup(LinkedList l, int m){
		Node front = null;
		Node current = l.front;
		Node start = l.front;
		
		int i = 0;
		
		while(current != null){
			i++;
			current = current.next;
			if(i == m){
				Node end = current;
				while(i > 0){
					Node next = start.next;
					start.next = end;
					end = start;
					start=next;
					i--;

				}
				
				if (front == null){
					front = l.front;
					l.front=end;
				}else{
					Node tmp = front.next;
					front.next=end;
					front = tmp;
				}
			}	
		}
		
	}

}
