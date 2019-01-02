package com.zk.modifier.second;

import com.zk.modifier.one.Model;
import org.junit.Test;

public class SubModelTest extends Model {

    /**
     * 访问修饰符测试
     * 子类访问的测试
     */
    @Test
    @SuppressWarnings("all")
    public void test() {
        SubModelTest subModel = new SubModelTest();
        /** 注意：即使【 Model subModel = new SubModelTest() 】这样申明，都不能直接访问 **/
        System.out.println(subModel.publicAttr);
//    System.out.println(subModel.defaultAttr);
        System.out.println(subModel.protectedAttr);
//    System.out.println(subModel.privateAttr);
    }


}
