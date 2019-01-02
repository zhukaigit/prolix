package com.zk.modifier.second;

import com.zk.modifier.one.Model;
import org.junit.Test;

public class DiffPackageTest {

    /**
     * 访问修饰符测试
     * 不同包下的测试
     */
    @Test
    public void test() {
        Model model = new Model();
        System.out.println(model.publicAttr);
//    System.out.println(model.defaultAttr);
//    System.out.println(model.protectedAttr);
//    System.out.println(model.privateAttr);
    }

}
