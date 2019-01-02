package com.zk.dataStructure;

import java.util.Arrays;

/**
 * 数值实现：栈
 * 特点：先进后出，后进先出
 * 操作：入栈时往数组尾部添加元素，出栈时将数据尾部元素移除
 */
public class ArrayStack_08 {

    private int[] data;
    private int size;//容器大小
    private int top ;

    public ArrayStack_08(int size) {
        this.size = size;
        data = new int[size];
        top = 0;
    }

    /**
     * 入栈
     */
    public boolean push(int item) {
        if (top == size) {//栈已满
            return false;
        }
        data[top] = item;
        top++;
        return true;
    }

    /**
     * 出栈
     */
    public Integer pop() {
        if (top == 0) {
            return null;
        }
        int removeItem = data[--top];
        data[top] = 0;
        return removeItem;
    }

    @Override
    public String toString() {
        return "ArrayStack_08{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
