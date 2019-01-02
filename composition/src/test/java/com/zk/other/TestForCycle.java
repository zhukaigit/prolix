package com.zk.other;

import org.junit.Test;

import java.util.Arrays;

/**
 * 第一种for循环：for (int i = 0; i < arr.length; i++) {}
 * 第二种for循环：for (Object ele : arr) {}
 * <p>
 * 区别：
 * 第一种：若arr数组指向的内存地址发生改变，那么arr[i]即为新地址所对应的对象。
 * <p>
 * 第二种：主要是对ele对象的理解，ele指向原始arr的内存地址，并循环下去，即使在循环的过程中arr被其他线程修改，指向了其他的内存地址，
 * ele依旧只是对原始内存地址的对象进行遍历。
 * <p>
 * 总结：第一种方式是对arr引用对象的遍历，此时arr指向什么，就取什么。而第二种是对arr原始内存对象的遍历，即使arr后面的引用指向了其他对象
 */
public class TestForCycle {
    StringBuilder s1 = new StringBuilder("11");
    StringBuilder s2 = new StringBuilder("22");
    StringBuilder s3 = new StringBuilder("33");
    StringBuilder s4 = new StringBuilder("44");
    StringBuilder[] arr = {s1, s2, s3, s4};

    @Test
    public void test1() throws Exception {
        new Thread(() -> print1()).start();
        Thread.sleep(100);
        new Thread(() -> changeArrAddress()).start();  //改变arr内存地址
        Thread.sleep(3000);
        //输出结果详见：【结果1】
    }

//    【结果1】
//    线程名：Thread-0, 第1个元素值：11, 数组对象：[11, 22, 33, 44], 数组hashCode：1291981629
//    线程名：Thread-0, 第2个元素值：22, 数组对象：[11, 22, 33, 44], 数组hashCode：1291981629
//    arr指针已改变，指向新的内存地址
//    线程名：Thread-0, 第3个元素值：33, 数组对象：[111, 222, 333, 444, 555], 数组hashCode：1137046765
//    线程名：Thread-0, 第4个元素值：44, 数组对象：[111, 222, 333, 444, 555], 数组hashCode：1137046765

    @Test
    public void test2() throws Exception {
        new Thread(() -> print1()).start();
        Thread.sleep(100);
        new Thread(() -> changeArrMemory()).start();    //不改变arr内存地址，只改变arr内部元素的值
        Thread.sleep(3000);
        //输出结果详见：【结果2】
    }

//    结果【2】
//    线程名：Thread-0, 第1个元素值：11, 数组对象：[11, 22, 33, 44], 数组hashCode：1291981629
//    线程名：Thread-0, 第2个元素值：22, 数组对象：[11, 22, 33, 44], 数组hashCode：1291981629
//    arr内容改变，内存地址不变
//    线程名：Thread-0, 第3个元素值：33--3, 数组对象：[11--1, 22--2, 33--3, 44--4], 数组hashCode：1291981629
//    线程名：Thread-0, 第4个元素值：44--4, 数组对象：[11--1, 22--2, 33--3, 44--4], 数组hashCode：1291981629

    @Test
    public void test3() throws Exception {
        new Thread(() -> print2()).start();
        Thread.sleep(100);
        new Thread(() -> changeArrAddress()).start();   //改变arr内存地址
        Thread.sleep(3000);
        //输出结果详见：【结果3】
    }

//    【结果3】
//    线程名：Thread-0, 第1个元素值：11, 数组对象：[11, 22, 33, 44], 数组hashCode：22560608
//    线程名：Thread-0, 第2个元素值：22, 数组对象：[11, 22, 33, 44], 数组hashCode：22560608
//    arr指针已改变，指向新的内存地址
//    线程名：Thread-0, 第3个元素值：333, 数组对象：[111, 222, 333, 444, 555], 数组hashCode：262132027
//    线程名：Thread-0, 第4个元素值：444, 数组对象：[111, 222, 333, 444, 555], 数组hashCode：262132027
//    线程名：Thread-0, 第5个元素值：555, 数组对象：[111, 222, 333, 444, 555], 数组hashCode：262132027

    @Test
    public void test4() throws Exception {
        new Thread(() -> print2()).start();
        Thread.sleep(100);
        new Thread(() -> changeArrMemory()).start();
        Thread.sleep(3000);
        //输出结果详见：【结果4】
    }

//    【结果4】
//    线程名：Thread-0, 第1个元素值：11, 数组对象：[11, 22, 33, 44], 数组hashCode：1291981629
//    线程名：Thread-0, 第2个元素值：22, 数组对象：[11, 22, 33, 44], 数组hashCode：1291981629
//    arr内容改变，内存地址不变
//    线程名：Thread-0, 第3个元素值：33--3, 数组对象：[11--1, 22--2, 33--3, 44--4], 数组hashCode：1291981629
//    线程名：Thread-0, 第4个元素值：44--4, 数组对象：[11--1, 22--2, 33--3, 44--4], 数组hashCode：1291981629

    //不改变arr内存地址，只改变arr数组中对象的内容
    public void changeArrMemory() {
        s1 = s1.append("--1");
        s2 = s2.append("--2");
        s3 = s3.append("--3");
        s4 = s4.append("--4");
        System.out.println("arr数组中对象内容改变，arr与arr数组内对象的内存地址都不变");
    }

    //对arr成员变量重新赋值，内存中指向另一个地址
    public void changeArrAddress() {
        StringBuilder[] arr2 = {
                new StringBuilder("111"), new StringBuilder("222"),
                new StringBuilder("333"), new StringBuilder("444"),
                new StringBuilder("555")
        };
        arr = arr2;
        System.out.println("arr指针已改变，指向新的内存地址");
    }

    //第一种方式遍历，遍历2个元素后休息0.5秒钟，再继续遍历
    public void print1() {
        int index = 0;
        for (StringBuilder ele : arr) {
            index++;
            System.out.println(String.format("线程名：%s, 第%s个元素值：%s, 数组对象：%s, 数组hashCode：%s",
                    Thread.currentThread().getName(), index, ele, Arrays.asList(arr), arr.hashCode()));
            if (index == 2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //第二种方式遍历，遍历2个元素后休息0.5秒钟，再继续遍历
    public void print2() {
        for (int j = 0; j < arr.length; j++) {
            System.out.println(String.format("线程名：%s, 第%s个元素值：%s, 数组对象：%s, 数组hashCode：%s",
                    Thread.currentThread().getName(), j + 1, arr[j], Arrays.asList(arr), arr.hashCode()));
            if (j == 1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
