package com.zk.feign;

import com.zk.feign.model.UserDto;
import feign.Headers;
import feign.Param;
import feign.Request;
import feign.RequestLine;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.SocketTimeoutException;

/**
 *
 * 注意：遇到的坑，之前依赖feign版本不是9.7.0，一直导致调用超时不正确
 */
@Slf4j
public class FeignTest {

    public interface RemoteClient {
        String BASE_URL = "http://localhost:8081/api";

        @RequestLine ("POST /queryUserDtoByName?name={name}")
        @Headers ("Content-Type: application/x-www-form-urlencoded")
        UserDto queryUserDtoByName(@Param("name") String name);

        // prolix下springboot-test应用下的url
        @RequestLine("GET /feign/sleep?sleepMills={sleepMills}")
        @Headers("Content-Type: application/x-www-form-urlencoded")
        String testTimeout(@Param("sleepMills") long sleepMills);
    }

    private long start = 0;
    private long end = 0;
    @Before
    public void before() {
        start = System.currentTimeMillis();
    }

    @After
    public void after() {
        end = System.currentTimeMillis();
        System.out.println("======== 执行时间：" + (float) (end - start) / 1000 + " =============");
    }

    /**
     * okhttpClient设置了readTimeout=10s，feign的Request.option设置了readTimeout=3s
     * 测试用例：feign Request.option时间 < 服务器sleep=6s < okhttpReadTimeout
     * 测试结果：超时异常
     *
     * 源码位置：feign.okhttp.OkHttpClient#execute(feign.Request, feign.Request.Options)，最终超时时间以feign设置为准
     */
    @Test
    public void testTimeOut1() {
        try {
            RemoteClient remoteClient = new FeignClientBuilder<RemoteClient>()
                    .buildRemoteClient(RemoteClient.class, RemoteClient.BASE_URL, 0,
                            FeignClientBuilder.getOkHttpClient(),
                            new Request.Options(500, 3000)
                    );
            start = System.currentTimeMillis();
            remoteClient.testTimeout(6000);
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        }
    }

    /**
     * okhttpClient设置了readTimeout=10s，feign的Request.option设置了readTimeout=14s
     * 测试用例：okhttpReadTimeout < 服务器sleep=12s < feign Request.option时间
     * 测试结果：未报超时异常
     *
     * 源码位置：feign.okhttp.OkHttpClient#execute(feign.Request, feign.Request.Options)，最终超时时间以feign设置为准
     */
    @Test
    public void testTimeOut2() {
        try {
            RemoteClient remoteClient = new FeignClientBuilder<RemoteClient>()
                    .buildRemoteClient(RemoteClient.class, RemoteClient.BASE_URL, 3,
                            FeignClientBuilder.getOkHttpClient(),
                            new Request.Options(1, 1)
                    );
            start = System.currentTimeMillis();
            remoteClient.testTimeout(12000);
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        }
    }

    @Test
    public void testFeignRetry() {
        try {
            RemoteClient remoteClient = new FeignClientBuilder<RemoteClient>()
                    .buildRemoteClient(RemoteClient.class, RemoteClient.BASE_URL, 5,
                            FeignClientBuilder.getOkHttpClient(),
                            new Request.Options(500, 3000)
                    );
            start = System.currentTimeMillis();
            remoteClient.testTimeout(20000);
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        }
    }

}




