package com.zk.feign.protogenesis;

import feign.Headers;
import feign.RequestLine;

import java.util.Map;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/14.
 */
public interface TestApi {

    String BASE_URL = "https://www.baidu.com";

    @Headers({"Accept: text/html"})
    @RequestLine("GET /")
    byte[] index();

    @RequestLine("POST /loan/queryStatus")
    Map<String, Object> queryStatus();

}
