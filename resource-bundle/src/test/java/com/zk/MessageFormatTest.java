package com.zk;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/24.
 */
public class MessageFormatTest {

    @Test
    public void test() {
        String value = MessageFormat.format("oh, ''{0}'' is a pig", "ZhangSan");
        System.out.println(value);
    }

    @Test
    public void test2() {
        String value = MessageFormat.format("oh, {0,number,#.##} is good num",
                new BigDecimal("1.125088"));
        System.out.println(value);
    }

    @Test
    public void test3() {
        String value = MessageFormat.format("oh, {0} is good num", Double.valueOf("3.1415"));
        System.out.println(value);
    }
}
