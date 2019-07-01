package com.zk.feign;

import com.zk.feign.model.UserDto;
import feign.Headers;
import feign.Param;
import feign.Request;
import feign.RequestLine;
import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import org.junit.Assert;
import org.junit.Test;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FeignTest {
    public interface TestControllerApi {
        String BASE_URL = "http://localhost:8081/springtest/feign";

        @RequestLine("POST /queryUserDtoByName?name={name}")
        @Headers("Content-Type: application/x-www-form-urlencoded")
        UserDto queryUserDtoByName(@Param("name") String name);
    }

    /**
     * 保证目标方法sleep超过readTimeoutMills
     */
    @Test
    public void testTimeOut() {
        long start = System.currentTimeMillis();
        try {
            TestControllerApi controllerApi = new TestFeignBuilder<TestControllerApi>()
                    .getApi(TestControllerApi.class, TestControllerApi.BASE_URL, 0,
                            new OkHttpClient(),
                            new Request.Options(500, 1500)
                    );
            controllerApi.queryUserDtoByName("zhukai");
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        } finally {
            System.err.println("方法执行时间：" + calculateTimePeriod(start, System.currentTimeMillis()));
        }
    }

    @Test
    public void testOkhttpclientTimeOut() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.MILLISECONDS)
                .readTimeout(1000, TimeUnit.MILLISECONDS)
                .writeTimeout(1000, TimeUnit.MICROSECONDS)
                .connectionPool(new ConnectionPool(10, 10, TimeUnit.MINUTES))
                .build();
        long start = System.currentTimeMillis();
        try {
            TestControllerApi controllerApi = new TestFeignBuilder<TestControllerApi>()
                    .getApi(TestControllerApi.class, TestControllerApi.BASE_URL, 0,
                            new OkHttpClient(client),
                            new Request.Options(500, 8000)
                    );
            controllerApi.queryUserDtoByName("zhukai");
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        } finally {
            System.err.println("方法执行时间：" + calculateTimePeriod(start, System.currentTimeMillis()));
        }

    }

    @Test
    public void testFeignRetry() {
        long start = System.currentTimeMillis();
        try {
            TestControllerApi controllerApi = new TestFeignBuilder<TestControllerApi>()
                    .getApi(TestControllerApi.class, TestControllerApi.BASE_URL, 1,
                            new OkHttpClient(),
                            new Request.Options(500, 3000)
                    );
            controllerApi.queryUserDtoByName("zhukai");
        } catch (Exception e) {
            log.error("调用异常", e);
            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
        } finally {
            System.err.println("方法执行时间：" + calculateTimePeriod(start, System.currentTimeMillis()));
        }

    }

    private float calculateTimePeriod(long start, long end) {
        return (float) (end - start) / 1000;
    }

}




