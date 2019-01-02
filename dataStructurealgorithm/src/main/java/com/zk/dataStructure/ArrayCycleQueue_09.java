package com.zk.dataStructure;

import java.util.Arrays;

/**
 * 入队时到尾部，出队时从头部
 * 特点：先入先出，后入后出
 */
public class ArrayCycleQueue_09<T> {

    private T[] list;
    private int head;
    private int tail;//指向尾部后一个元素
    private int capacity;
    private int size;

    public ArrayCycleQueue_09(int capacity) {
        this.capacity = capacity;
        Object[] objects = new Object[capacity];
        list = (T[]) objects;
    }

    /**
     * 入队到tail，若tail已到数组最后，插入到数组头部。不用搬移数据
     */
    public boolean enqueue(T t) {
        if (size == capacity) {
            System.out.println("【error】" + t + "入队失败，队列已满");
            return false;
        }

        list[tail] = t;
        incrementTail();
        size++;
        System.out.println(t + " ====> 入队");
        return true;
    }

    /**
     * 出队
     * 从head取元素
     */
    public T dequeue() {
        if (size == 0) {
            System.out.println("【error】队列为空");
            return null;
        }
        T removeData = list[head];
        list[head] = null;
        incrementHead();
        size--;
        System.out.println(removeData + " ====> 出队");
        return removeData;
    }

    @Override
    public String toString() {
        return "ArrayQueue_09{" +
                "list=" + Arrays.toString(list) +
                '}';
    }

    private void incrementTail() {
        if (tail == capacity - 1) {
            tail = 0;
        } else {
            tail++;
        }
    }

    private void incrementHead() {
        if (head == capacity - 1) {
            head = 0;
        } else {
            head++;
        }
    }
}
