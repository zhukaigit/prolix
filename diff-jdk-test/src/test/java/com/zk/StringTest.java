package com.zk;

import org.junit.Test;

/**
 * 字符串单侧类
 * 注意：要逐个执行每个单侧方法，不要统一执行该单测类所有方法，因为“ab”生成后会对之后的方法有影响
 */
public class StringTest {

    @Test
    public void test () {
        /*
            根据构造函数入参“ab”先生成常量池的对象，然后再在堆中生成“ab”字符串对象，这个语句相当于如下两行代码
            String s = "ab";
            String s1 = new String(s)
         */
        String s1 = new String("ab");
        String s2 = "ab";// 此时常量池中已存在“ab”值（即隐藏的s）
        System.out.println(s1 == s2); // false(jdk6,7,8)
    }

    /**
     * 1、在jdk6中的常量池是放在 Perm 区中的，Perm 区和正常的 JAVA Heap 区域是完全分开的,而 new 出来的 String 对象是放在 JAVA Heap 区域。所以拿一个 JAVA Heap
     * 区域的对象地址和字符串常量池的对象地址进行比较肯定是不相同的，即使调用String.intern方法也是没有任何关系的。
     * 2、在jdk7中的字符串常量池已经从 Perm 区移到正常的 Java Heap 区域了，在jdk8中取消了Perm区,元空间取代了Perm区。当使用intern()时堆内存中存在对象内容是”某字符串”,
     * 但此时常量池中是没有 “某字符串”对象,常量池中不需要再存储一份对象了，可以直接存储堆中的引用。
     */
    @Test
    public void test2 () {
        String s1 = new String("a") + new String("b");// 该条语句执行完之后，此时常量池还没有“ab”字符串
        s1.intern();// 该条语句执行完之后，若是jdk1.6，会在常量池新增一个“ab”字符串对象，若是jdk1.7及其之后的版本，则常量池中的保存引用，该引用指向s1内存地址，以节省内存开销
        String s2 = "ab";// 常量池中的对象
        System.out.println(s1 == s2);// false(jdk6), true(jdk7,8)
    }

    @Test
    public void test3 () {
        String s1 = new String("ab");// 该条语句执行完之后，此时常量池还没有“ab”字符串
        s1.intern();// 该条语句执行完之后，若是jdk1.6，会在常量池新增一个“ab”字符串对象，若是jdk1.7及其之后的版本，则常量池中的保存引用，该引用指向s1内存地址，以节省内存开销
        String s2 = "ab";// 常量池中的对象
        System.out.println(s1 == s2);// false(jdk6,7,8)
    }

    @Test
    public void test4 () {
        String s1 = new String("a") + new String("b");// 该条语句执行完之后，此时常量池还没有“ab”字符串
        String s2 = "ab";// 由于此时常量池中没有“ab”值，所以会新增一个“ab”
        s1.intern();
        System.out.println(s1 == s2);// false(jdk6,7,8)
    }
}
