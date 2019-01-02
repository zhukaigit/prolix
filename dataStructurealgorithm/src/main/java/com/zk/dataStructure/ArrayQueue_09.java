package com.zk.dataStructure;

import java.util.Arrays;

/**
 * 数组实现的队列
 */
public class ArrayQueue_09<T> {

    private T[] list;
    private int head;
    private int tail;//指向尾部后一个元素
    private int capacity;
    private int size;

    public ArrayQueue_09(int capacity) {
        this.capacity = capacity;
        Object[] objects = new Object[capacity];
        list = (T[]) objects;
    }

    /**
     * 入队到数组后面
     */
    public boolean enqueue(T t) {
        if (size == capacity) {
            return false;
        }
        if (tail == capacity) {//当到达数组尾部时，集中搬移数据，统一向前移动head位
            for (int i = head; i < tail; i++) {
                list[i - head] = list[i];
                list[i] = null;
            }
            head = 0;
            tail = size;
        }

        list[tail] = t;
        tail++;
        size++;
        return true;
    }

    /**
     * 出队
     * 从数组头部取元素
     */
    public T dequeue() {
        if (size == 0) {
            return null;
        }
        T removeData = list[head];
        list[head] = null;
        head++;
        size--;
        return removeData;
    }

    @Override
    public String toString() {
        return "ArrayQueue_09{" +
                "list=" + Arrays.toString(list) +
                '}';
    }
}
