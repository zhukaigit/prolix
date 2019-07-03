package com.zk.feign;

import com.zk.feign.model.UserDto;
import feign.Headers;
import feign.Param;
import feign.Request;
import feign.RequestLine;
import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

/**
 *
 * 注意：遇到的坑，之前依赖feign版本不是9.7.0，一直导致调用超时不正确
 */
@Slf4j
public class FeignTest {
    public interface TestControllerApi {
        String BASE_URL = "http://localhost:8081/api";

        @RequestLine("POST /queryUserDtoByName?name={name}")
        @Headers("Content-Type: application/x-www-form-urlencoded")
        UserDto queryUserDtoByName(@Param("name") String name);

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
     * 保证目标方法sleep超过readTimeoutMills
     */
    @Test
    public void testTimeOut() {
        try {
            TestControllerApi controllerApi = new TestFeignBuilder<TestControllerApi>()
                    .getApi(TestControllerApi.class, TestControllerApi.BASE_URL, 0,
                            new OkHttpClient(),
                            new Request.Options(500, 5000)
                    );
            start = System.currentTimeMillis();
            controllerApi.testTimeout(20000);
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        }
    }

    @Test
    public void testOkhttpclientTimeOut() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .writeTimeout(1000, TimeUnit.MICROSECONDS)
                .connectionPool(new ConnectionPool(10, 10, TimeUnit.MINUTES))
                .build();
        try {
            TestControllerApi controllerApi = new TestFeignBuilder<TestControllerApi>()
                    .getApi(TestControllerApi.class, TestControllerApi.BASE_URL, 0,
                            new OkHttpClient(client),
                            new Request.Options(500, 8000)
                    );
            start = System.currentTimeMillis();
            controllerApi.testTimeout(20000);
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        }

    }

    @Test
    public void testFeignRetry() {
        try {
            TestControllerApi controllerApi = new TestFeignBuilder<TestControllerApi>()
                    .getApi(TestControllerApi.class, TestControllerApi.BASE_URL, 1,
                            new OkHttpClient(),
                            new Request.Options(500, 3000)
                    );
            start = System.currentTimeMillis();
            controllerApi.testTimeout(20000);
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        }

    }

}




