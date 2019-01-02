package com.zk.modifier.one;

import org.junit.Test;

public class SamePackageTest {

    /**
     * 访问修饰符测试
     * 统一包下的测试
     */
    @Test
    public void test() {
        Model model = new Model();
        System.out.println(model.publicAttr);
        System.out.println(model.defaultAttr);
        System.out.println(model.protectedAttr);
//    System.out.println(model.privateAttr);
    }

}
