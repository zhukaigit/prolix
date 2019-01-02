package com.zk.dataStructure;

import lombok.AllArgsConstructor;

/**
 * 链表实现队列
 */
public class LinkedQueue_09<T> {

    private Node<T> head;
    private Node<T> tail;

    public boolean enqueue(T t) {
        Node<T> newNode = new Node<>(t, null);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        System.out.println(t + " ====> 入队");
        return true;
    }

    public T dequeue() {
        if (head == null) {
            System.out.println("【error】队列为空");
            return null;
        } else {
            Node<T> removeNode = head;
            head = removeNode.next;
            removeNode.next = null;
            System.out.println(removeNode.data + " ====> 出队");
            return removeNode.data;
        }
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
    public static class Node<T> {
        private T data;
        private Node<T> next;
    }

}
