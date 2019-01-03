package com.zk;


import com.zk.inter.People;
import com.zk.inter.impl.Man;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2017/12/5.
 */
public class CommonTest {


    @Test
    public void test2() throws NoSuchMethodException {
        Method method = People.class.getDeclaredMethod("say", String.class);
        Method method2 = Man.class.getDeclaredMethod("say", String.class);
        System.out.println(method.getDefaultValue());
        System.out.println(method2.getDefaultValue());
    }

    public void t(Integer... integers) {
        System.out.println(Arrays.asList(integers));
    }

    @Test
    public void test() {
        Method method = null;
        try {
            method =People.class.getDeclaredMethod("say", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Man man = new Man();
        Object invoke = null;
        try {
            invoke = method.invoke(man, "朱凯");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println("result = " + invoke);
    }

}
