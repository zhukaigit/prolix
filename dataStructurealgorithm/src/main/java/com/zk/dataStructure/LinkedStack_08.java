package com.zk.dataStructure;

import lombok.AllArgsConstructor;

public class LinkedStack_08 {
    private Node head;

    public boolean push(int ele) {
        head = new Node(ele, head);
        return true;
    }

    public Integer pop() {
        if (head == null) {
            return null;
        }
        Node removeNode = this.head;
        head = head.next;
        removeNode.next = null;
        return removeNode.data;
    }

    @Override
    public String toString() {
        if (head != null) {
            StringBuffer result = new StringBuffer("[");
            Node current = head;
            if (current != null) {
                result.append(current.data);
            }
            while ((current = current.next) != null) {
                result.append(",").append(current.data);
            }
            result.append("]");
            return result.toString();
        }
        return null;
    }

    @AllArgsConstructor
    public static class Node {
        private int data;
        private Node next;
    }
}
