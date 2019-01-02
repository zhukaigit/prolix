package com.zk.dataStructure;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用哨兵模式
 */
@SuppressWarnings("all")
public class SingleChain2<E> {

    @Getter
    @Setter
    private Node<E> head = new Node<E>(null, null);//带头哨兵
    private AtomicInteger size = new AtomicInteger(0);

    public int size() {
        return size.get();
    }

    /**
     * 元素添加到链表头部
     * 时间复杂度：o(1)
     */
    public void addHead(E e) {
        Objects.requireNonNull(e, "the container cannot hold null");
        head.next = new Node<E>(e, head.next);
        size.getAndIncrement();
    }

    /**
     * 元素添加到链表尾部
     * 时间复杂度：o(n)
     */
    public void addTail(E e) {
        Objects.requireNonNull(e, "the container cannot hold null");
        Node<E> current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = new Node<E>(e, null);
        size.getAndIncrement();
    }

    /**
     * 删除元素
     */
    public boolean remove(E e) {
        Objects.requireNonNull(e, "the param cannot be null");

        Node<E> current = head;
        while (current.next != null) {
            Node prev = current;
            current = current.next;
            if (e.equals(current.data)) {
                prev.next = current.next;
                size.decrementAndGet();
                return true;
            }
        }
        return false;
    }

//    @Override
//    public String toString() {
//        Node current = head.next;
//        if (current == null) {
//            return "[]";
//        }
//        StringBuffer result = new StringBuffer("[");
//        if (current != null) {
//            result.append(current.data);
//        }
//        while ((current = current.next) != null) {
//            result.append(",").append(current.data);
//        }
//        result.append("]");
//        return result.toString();
//    }

    /**
     * 单链表反转
     */
    public void reverseNote() {
        Node<E> chainData = head.next;//链表数据
        Node<E> current = chainData;
        Node<E> next = current == null ? null : current.next;
        //链表中最多只有一个元素时，不需要反转
        if (next == null) return;

        //将原始的头元素纪录，最后需要指向null
        Node<E> sourceHead = current;

        //循环反转
        while (next != null) {
            Node<E> prev = current;
            current = next;
            next = next.next;
            //指针反转
            current.next = prev;
        }
        sourceHead.next = null;
        head.next = current;
    }

    /**
     * 链表中环的检测
     */
    public Map checkChainCycle() {
        HashMap<String, Object> map = new HashMap<>();
        int cycleLength = 0;//环的长度
        Node<E> portalNode = null;//环的入口节点
        int chainLength = 0;//链的总长度

        ArrayList<Node> nodes = new ArrayList<>();
        Node<E> current = head.next;
        Node<E> next;
        while (current != null) {//时间复杂度为o(n^2)
            chainLength++;
            //判断current的下一个节点在不在notes中
            next = current.next;
            Node<E> finalNext = next;
            Optional<Node> noteOptional = nodes.stream().filter(note -> note == finalNext).findAny();
            if (noteOptional.isPresent()) {
                portalNode = next;
                break;
            } else {
                nodes.add(current);
                current = next;
            }
        }

        //判断portalNote在notes中位置，时间复杂度为o(n)
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) == portalNode) {
                cycleLength = chainLength - i;
            }
        }

        map.put("cycleLength", cycleLength);
        map.put("portalNote", portalNode);
        return map;
    }

    @lombok.Builder
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    @lombok.Getter
    @lombok.Setter
    private static class Node<T> {
        //    private Node<T> header;
        private T data;
        private Node<T> next;

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    '}';
        }
    }

    public static void main(String[] args) {
        SingleChain2 chain = new SingleChain2();
        for (int i = 1; i <= 5; i++) {
            chain.addTail(i);
        }
        System.out.println("链中是否有环：" + chain.checkChainCycle());

        //构造环 -- 开始
        Node current = chain.getHead();
        while (current.next != null) {
            current = current.next;
        }
        current.next = chain.getHead().next.next;
        //构造环 -- 结束

        System.out.println("链中是否有环：" + chain.checkChainCycle());

    }

}
