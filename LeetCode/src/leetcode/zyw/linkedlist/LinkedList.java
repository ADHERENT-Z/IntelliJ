package leetcode.zyw.linkedlist;

public class LinkedList {

    public static Node createLinkedList(int[] arr, boolean headFlag) {

        Node head = new Node(0);
        Node cur = head;
        for (int num : arr) {
            cur.next = new Node(num);
            cur = cur.next;
        }
        if (!headFlag) {
            head = head.next;
        }

        return head;
    }


    public static Node getLoopNode(Node head) {
        Node slow = head, fast = head;
        boolean hasCycle = false;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (fast == slow) {
                hasCycle = true;
                break;
            }
        }
        if (!hasCycle) {
            return null;
        }
        slow =  head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }


    public static void printLinkedList(Node head) {

        while (head != null) {
            System.out.print(head.value + "->");
            head = head.next;
        }
        System.out.println("null");
    }

    /**
     * 向链表添加数据
     * @param value 要添加的数据
     */
    public static void addNode(int value) {

    }


    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7};
        Node head = createLinkedList(arr, false);
        printLinkedList(head);
//        System.out.println(getLoopNode(head));
    }
}
