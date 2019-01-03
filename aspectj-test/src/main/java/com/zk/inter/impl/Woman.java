package com.zk.inter.impl;

import com.zk.inter.People;
import org.springframework.stereotype.Component;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2017/12/3.
 */
@Component
public class Woman implements People {

    public String say(String name) {
        System.out.println("i'm a Woman , name = " + name);
        return name;
    }
}
