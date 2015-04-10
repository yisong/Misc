/***

Merge k sorted linked lists and return it as one sorted list. 
Analyze and describe its complexity.

***/

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public ListNode mergeKLists(List<ListNode> lists) {
       PriorityQueue<ListNode> p = new PriorityQueue(
           10, 
           new Comparator<ListNode>() {
               @Override
                public int compare(ListNode l1, ListNode l2) {
                    return l1.val - l2.val;
                }
            }
           );
       for (ListNode node : lists) {
           if (node != null) {
               p.add(node);
           }
       }
       
       ListNode result = null;
       ListNode cur = null;
       while (!p.isEmpty()) {
           ListNode node = p.poll();
 
           if (result == null) {
               result = node;
           } else {
               cur.next = node;
           }
           cur = node;

            if (node.next != null) {
               p.add(node.next);
           }
       }
       
       return result;
    }
}
