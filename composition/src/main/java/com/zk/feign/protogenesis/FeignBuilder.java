package com.zk.feign.protogenesis;

import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import okhttp3.ConnectionPool;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/14.
 */
public class FeignBuilder<T> {

    public T getApi(Class<T> tClass, String baseUrl) {
        return getApi(tClass, baseUrl, true);
    }


    public T getApi(Class<T> tClass, String baseUrl, boolean withDecoder) {
        return getApi(tClass, baseUrl, withDecoder, 0);
    }


    /**
     * @param retryTime 重试的次数
     * @return
     */
    public T getApi(Class<T> tClass, String baseUrl, boolean withDecoder, int retryTime) {
        Feign.Builder builder = Feign.builder()
                .encoder(new JacksonEncoder())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.BASIC)
                .client(getOkHttpClient())
                .errorDecoder(new MyErrorDecoder()) //feign client把非200的以及404(可以配置是否纳入异常)都算成error，都转给errorDecoder去处理了
//                .requestInterceptor(new MyRequestInterceptor())
                .retryer(new Retryer.Default(100, SECONDS.toMillis(1), 3))
                ;

        if (withDecoder) {
            builder.decoder(new JacksonDecoder());
        }
        if (retryTime > 0) {
            builder.options(new Request.Options(1000, 2000))//超时时间设置
                    .retryer(new Retryer.Default(100, 500, retryTime + 1));//period：表示每次重试的间隔时间。需要与options搭配使用

        }
        return builder.target(tClass, baseUrl);
    }

    private OkHttpClient getOkHttpClient() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(10, 10, TimeUnit.MINUTES))
                .build();
        return new OkHttpClient(client);
    }

}
