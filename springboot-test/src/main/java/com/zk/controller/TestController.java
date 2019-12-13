package com.zk.controller;

import com.zk.common.BaseRequest;
import com.zk.common.BaseResponse;
import com.zk.common.annotation.ResponseLog;
import com.zk.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
@ResponseLog
@Api(tags = "测试操作类")
public class TestController {

    @Resource (name = "commonThreadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1);

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



    @GetMapping ("/test/sleuth")
    public String testSleuth () {
        log.info("请求时间：{}", new Date().toLocaleString());

        // new出来的线程，不会加入trace
        new Thread(new Runnable() {
            @Override
            public void run () {
                log.info("test trace id in new thread");
            }
        }).start();

        // 不会加入trace
        threadPool.execute(new Runnable() {
            @Override
            public void run () {
                log.info("test trace id in thread pool");
            }
        });


        // 只有Spring管理的线程池，线程执行会加入trace
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run () {
                log.info("test trace id in spring manager thread pool");
            }
        });


        return new Date().toString();
    }



}
