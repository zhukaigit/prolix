package com.zk.dataStructure;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("all")
public class SingleChain<E> {

    private Note<E> head;
    private AtomicInteger size = new AtomicInteger(0);

    public int size() {
        return size.get();
    }

    /**
     * 元素添加到链表头部
     * 时间复杂度：O(1)
     */
    public void addHead(E e) {
        Objects.requireNonNull(e, "the container cannot hold null");
        head = new Note<E>(e, head);
        size.getAndIncrement();
    }

    /**
     * 元素添加到链表尾部
     * 时间复杂度：O(n)
     */
    public void addTail(E e) {
        Objects.requireNonNull(e, "the container cannot hold null");
        if (head == null) {
            head = new Note<E>(e, null);
        } else {
            Note<E> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Note<E>(e, null);
        }
        size.getAndIncrement();
    }

    /**
     * 删除元素
     */
    public void remove(E e) {
        if (e == null || head == null) {
            return;
        }

        Note current = head;

        //头节点
        if (e.equals(current.data)) {
            head = head.next;
            size.decrementAndGet();
            return;
        }

        while (current.next != null) {
            Note prev = current;
            current = current.next;
            if (e.equals(current.data)) {
                prev.next = current.next;
                size.decrementAndGet();
                return;
            }
        }
    }

    @Override
    public String toString() {
        if (head != null) {
            StringBuffer result = new StringBuffer("[");
            Note current = head;
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

    @lombok.Builder
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    @lombok.ToString
    @lombok.Getter
    @lombok.Setter
    private static class Note<T> {
        //    private Note<T> header;
        private T data;
        private Note<T> next;

    }

}
