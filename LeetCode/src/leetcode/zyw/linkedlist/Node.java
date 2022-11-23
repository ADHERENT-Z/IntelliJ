package leetcode.zyw.linkedlist;

public class Node {

    public int value;

    //指针域，指向下一个节点
    public Node next;

    public Node(){

    }

    public Node(int value) {
        this.value = value;
        this.next = null;
    }
}
