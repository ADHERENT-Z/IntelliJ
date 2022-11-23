package leetcode.zyw.twonumadd;

public class Solution {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head  = null, tail = null;
        //定义一个进位数的指针，用来存储当两数之和大于10的时候
        int carry = 0;
        //当l1不等于null或l2不等于null时进入循环
        while (l1 != null || l2 != null) {
            //如果l1 不等于null时，就取他的值，等于null时，就赋值0，保持两个链表具有相同的位数
            int n1 = l1 != null ? l1.val : 0;
            //如果l2 不等于null时，就取他的值，等于null时，就赋值0，保持两个链表具有相同的位数
            int n2 = l2 != null ? l2.val : 0;
            //将两个链表的值，进行相加，并加上进位数
            int sum = n1 + n2 + carry;

            if (head == null) {
                head = tail = new ListNode(sum % 10);
            } else {
                tail.next = new ListNode(sum % 10);
                tail = tail.next;
            }
            //计算进位数
            //计算两个数的和，此时排除超过10的请况（大于10，取余数）
            carry = sum % 10;
            //当链表l1不等于null的时候，将l1 的节点后移
            if (l1 != null) {
                l1 = l1.next;
            }
            //当链表l2 不等于null的时候，将l2的节点后移
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            tail.next = new ListNode(carry);
        }
        return head;
    }
}
