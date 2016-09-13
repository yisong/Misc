/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        
        ListNode head = null;
        ListNode cur = null;
        while(l1 != null && l2 != null) {
            if (l1.val > l2.val) {
                ListNode tmp = l1;
                l1 = l2;
                l2 = tmp;
            }
                
            if (head == null) {
                head = l1;
                cur = l1;
            } else {
                cur.next = l1;
                cur = l1;
            }
            l1 = l1.next;
        }
        if (l1 == null) {
            l1 = l2;
        }
        if (head == null) {
            head = l1;
        } else {
            cur.next = l1;
        }
        return head;
        
    }
}
