package com.zk.other;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OtherTest {

    /**
     * 测试：Collections.unmodifiableList(list)。
     * 返回指定列表的不可修改视图。 该方法允许模块向用户提供对内部列表的“只读”访问。
     * 将返回的列表上的查询操作“读取”到指定的列表，并尝试修改返回的列表，无论是直接还是通过其迭代器，
     * 都会导致UnsupportedOperationException 。
     */
    @Test
    public void test1() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4);
        List<Integer> unmodifiableList = Collections.unmodifiableList(list);
        System.out.println("unmodifiableList = " + unmodifiableList);
        unmodifiableList.add(8);
    }

    @Test
    public void testGetSuperclass() {
        Class<? super A> superclass = A.class.getSuperclass();
        while (true) {
            if (superclass == null) break;
            System.out.println("superClass = " + superclass);
            superclass = superclass.getSuperclass();
        }
    }

    public static class A extends OtherTest implements Comparable, Runnable {
        @Override
        public int compareTo(Object o) {
            return 0;
        }

        @Override
        public void run() {
        }
    }

}
