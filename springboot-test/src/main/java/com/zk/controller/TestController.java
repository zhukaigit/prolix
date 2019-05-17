package com.zk.controller;

import com.zk.common.BaseRequest;
import com.zk.common.BaseResponse;
import com.zk.common.annotation.ResponseLog;
import com.zk.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
@ResponseLog
public class TestController {

    @RequestMapping ("/health")
    public String ok () {
        return "ok, time = " + new Date().toLocaleString();
    }

    @PostMapping ("/test/json")
    public String testJson (@RequestBody Map body) {
        log.info("请求体：{}", JsonUtils.toJsonHasNullKey(body));
        return JsonUtils.toJsonHasNullKey(body);
    }

    @PostMapping ("/test/requestBody")
    public String testRequestBody (String name) {
        log.info("请求参数：{}", name);
        return name;
    }

    @PostMapping ("/test/aop")
    public BaseResponse<Map> testAop (boolean error, @RequestBody BaseRequest<Map> body) {
        log.info("请求体：{}", JsonUtils.toJsonHasNullKey(body));
        if (error) {
            int i = 1 / 0;
        }
        return BaseResponse.success(body.getData());
    }

}
