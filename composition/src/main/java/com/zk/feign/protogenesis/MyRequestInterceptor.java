package com.zk.feign.protogenesis;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/14.
 */
@Slf4j
public class MyRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("username", "zhukai");
        template.header("password", "123456");
    }
}
