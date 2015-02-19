/***
Given a linked list, determine if it has a cycle in it.

Follow up:
Can you solve it without using extra space?
***/

/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public boolean hasCycle(ListNode head) {
        ListNode a = head;
        ListNode b = head;
        while(a != null && b != null) {
            a = a.next;
            b = b.next;
            if (b == null) {
                return false;
            }
            b = b.next;
            if (a == b) {
                return true;
            }
        }
        return false;
    }
}
