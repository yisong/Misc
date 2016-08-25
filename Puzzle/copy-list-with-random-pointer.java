/*

A linked list is given such that each node contains an additional random pointer which could point to any node in the list or null.

Return a deep copy of the list.

*/

/**
 * Definition for singly-linked list with a random pointer.
 * class RandomListNode {
 *     int label;
 *     RandomListNode next, random;
 *     RandomListNode(int x) { this.label = x; }
 * };
 */
public class Solution {
    public RandomListNode copyRandomList(RandomListNode head) {
       
       if (head == null) {
           return null;
       }
        RandomListNode cur = head;
        Map<Integer, RandomListNode> m = new HashMap<Integer, RandomListNode>();
        RandomListNode copy = new RandomListNode(cur.label);
        m.put(cur.label, copy);
       
        while (cur != null) {
            if (cur.random != null) {
                RandomListNode copyrandom = m.get(cur.random.label); 
                if (copyrandom == null) {
                  copyrandom = new RandomListNode(cur.random.label);
                  m.put(cur.random.label, copyrandom);
                }
                copy.random = copyrandom;
            } else {
                copy.random = null;
            }

            if (cur.next != null) {
                RandomListNode copynext = m.get(cur.next.label); 
                if (copynext == null) {
                  copynext = new RandomListNode(cur.next.label);
                  m.put(cur.next.label, copynext);
                }
                copy.next = copynext;
            } else {
                copy.next = null;
            }
            
            cur = cur.next;
            copy = copy.next;
       }
        return m.get(head.label); 
    }
}
