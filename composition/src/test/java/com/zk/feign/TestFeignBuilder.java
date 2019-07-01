package com.zk.feign;

import com.zk.feign.protogenesis.MyErrorDecoder;
import com.zk.utils.JsonUtils;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;

import java.util.concurrent.TimeUnit;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/14.
 */
@Slf4j
public class TestFeignBuilder<T> {


    /**
     * @param retryTime 重试的次数
     * @return
     */
    public T getApi(Class<T> tClass, String baseUrl, int retryTime, OkHttpClient client, Request.Options options) {
        Feign.Builder builder = new Feign.Builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .errorDecoder(new MyErrorDecoder()) //feign client把非200的以及404(可以配置是否纳入异常)都算成error，都转给errorDecoder去处理了
//                .requestInterceptor(new MyRequestInterceptor())
                .client(client)
                .options(options);

        if (retryTime > 0) {
            //period：表示每次重试的间隔时间。需要与options搭配使用
            builder.retryer(new Retryer.Default(100, 500, retryTime + 1));
        } else {
            log.info("===== 无重试 ======");
            builder.retryer(Retryer.NEVER_RETRY);
        }
        log.info("feign builder = {}", JsonUtils.toJsonHasNullKey(builder));
        return builder.target(tClass, baseUrl);
    }

    private OkHttpClient getOkHttpClient() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(120000, TimeUnit.MILLISECONDS)
                .writeTimeout(120000, TimeUnit.MICROSECONDS)
                .connectionPool(new ConnectionPool(10, 10, TimeUnit.MINUTES))
                .build();
        return new OkHttpClient(client);
    }

}
