/***

You are given two linked lists representing two non-negative numbers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.

Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 0 -> 8

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
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return helper(l1, l2, false);
    }
    
    private ListNode helper(ListNode l1, ListNode l2, boolean incre) {
        if (l1 == null && l2 == null && !incre) {
            return null;   
        }
        int x = l1 == null ? 0 : l1.val;
        int y = l2 == null ? 0 : l2.val;
        
        int total = x + y + (incre ? 1 : 0);
        boolean nextIncr = total >= 10;
        ListNode lSum = new ListNode(total % 10);
        lSum.next = helper(
            l1 != null ? l1.next : null,
            l2 != null ? l2.next : null,
            nextIncr
        );
        return lSum;
    }

}
