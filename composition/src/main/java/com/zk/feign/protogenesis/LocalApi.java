package com.zk.feign.protogenesis;

import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/14.
 */
public interface LocalApi {

    java.lang.String BASE_URL = "http://localhost:8080";

    @RequestLine("POST /test/sleep/10s")
    Object testSleep10s(@QueryMap Map<String, String> queryMap);

    @RequestLine("POST /feign/success")
    Object success();

    @RequestLine("POST /feign/error")
    Object error();

}
