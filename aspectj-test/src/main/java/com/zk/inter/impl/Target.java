package com.zk.inter.impl;

import com.zk.annotation.Api;
import com.zk.annotation.Encrypt;
import com.zk.annotation.IpWhiter;
import com.zk.annotation.ParamName;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2017/12/4.
 */
@Component
@IpWhiter
@Encrypt
@Api
public class Target {

    public void go() {
        System.out.println(String.format("Target Class annotation = %s",
                Arrays.asList(this.getClass().getDeclaredAnnotations())));
    }
}
