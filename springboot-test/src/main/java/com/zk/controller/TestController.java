package com.zk.controller;

import com.zk.common.BaseRequest;
import com.zk.common.BaseResponse;
import com.zk.common.annotation.ResponseLog;
import com.zk.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
@ResponseLog
@Api(tags = "测试操作类")
public class TestController {

    @GetMapping ("/health")
    @ApiOperation("健康检查")
    public String ok () {
        log.info("target method");
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
    public BaseResponse<Map> testAop (
            @RequestBody BaseRequest<Map> body,
            @RequestHeader("error") boolean error
    ) {
        log.info("请求体：{}", JsonUtils.toJsonHasNullKey(body));
        if (error) {
            int i = 1 / 0;
        }
        return BaseResponse.success(body.getData());
    }



}
