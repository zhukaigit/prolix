package com.zk.inter.impl;

import com.zk.inter.People;
import org.springframework.stereotype.Component;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2017/12/3.
 */
@Component
public class Father {

    public String say(String name) {
        if ("朱凯".equals(name)) {
            throw new RuntimeException("参数校验失败");
        }
        System.out.println("i'm a man , name = " + name);
        return name;
    }
}
