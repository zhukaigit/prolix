package com.zk.modifier.one;

import org.junit.Test;

public class Model {

    /**
     * 访问限制：无
     */
    public String publicAttr = "publicAttr_value";
    /**
     * 访问限制：本类、子类【即使在其他包中】、同一包中
     * 注意：不能被子包访问
     */
    protected String protectedAttr = "protectedAttr_value";
    /**
     * 即不加任何访问修饰符，通常称为“默认访问模式“。
     * 访问限制：同一包中
     * 注意：不能被子包访问
     */
    String defaultAttr = "defaultAttr_value";
    /**
     * 访问限制：本类
     */
    private String privateAttr = "privateAttr_value";

    /**
     * 访问修饰符测试
     * 本类中访问测试
     */
    @Test
    @SuppressWarnings("all")
    public void test() {
        Model model = new Model();
        System.out.println(model.publicAttr);
        System.out.println(model.defaultAttr);
        System.out.println(model.protectedAttr);
        System.out.println(model.privateAttr);
    }
}
