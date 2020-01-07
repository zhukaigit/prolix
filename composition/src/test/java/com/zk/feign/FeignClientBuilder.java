package com.zk.feign;

import com.zk.feign.protogenesis.MyErrorDecoder;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhukai on 2018/7/14.
 */
@Slf4j
public class FeignClientBuilder<T> {

    private static volatile OkHttpClient okHttpClient;
    private static final Object LOCK = new Object();
    private static final int readTimeout = 10000;
    private static final int writeTimeout = 10000;
    private static final int connectTimeout = 5000;

    public T buildRemoteClient (Class<T> respClass, String baseUrl) {
        return buildRemoteClient(respClass, baseUrl, 0, getOkHttpClient(), null);
    }

    public T buildRemoteClient (Class<T> respClass, String baseUrl, int retryTime, OkHttpClient client, Request.Options options) {
        Feign.Builder builder = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger())    // 日志使用okhttp定制化日志拦截器打印日志，这里不需要设置，设置也可以
                .logLevel(Logger.Level.NONE)    // 使用okhttp定制化日志拦截器打印日志
                .errorDecoder(new MyErrorDecoder()) //feign client把非200的以及404(可以配置是否纳入异常)都算成error，都转给errorDecoder去处理了
//                .requestInterceptor(new MyRequestInterceptor())
                .client(client);
        if (options == null) {
            // 默认：连接超时：10000, 读取超时：60000。这里修改默认配置
            builder.options(new Request.Options(connectTimeout, readTimeout));
        }else {
            builder.options(options);
        }
        if (retryTime > 0) {
            //period：表示每次重试的间隔时间，注意：间隔时间需小于读取超时时间，即options中的参数
            // maxAttempts：若为5，表示最多调用服务端5次（包括第一次的调用，即失败后会重试4次）
            builder.retryer(new Retryer.Default(100, 100, retryTime + 1));// 默认：100,1000,5
        } else {
            builder.retryer(Retryer.NEVER_RETRY);
        }
        return builder.target(respClass, baseUrl);
    }

    // 单例模式
    public static OkHttpClient getOkHttpClient () {
        if (okHttpClient == null) {
            synchronized (LOCK) {
                if (okHttpClient == null) {
                    okHttpClient = getClient();
                }
            }
        }
        return okHttpClient;
    }

    private static OkHttpClient getClient() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MICROSECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(10, 10, TimeUnit.MINUTES))
                .addInterceptor(new OkhttpLoggingInterceptor())
                .build();
        return new OkHttpClient(client);
    }

    /**
     * 定制化okhttp客户端日志拦截器
     */
    public static class OkhttpLoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            okhttp3.Request request = chain.request();

            try {
                // 获取请求报文体
                String requestBody = null;
                Buffer buffer = new Buffer();
                RequestBody body = request.body();
                if (body != null) {
                    body.writeTo(buffer);
                    InputStream inputStream = buffer.inputStream();
                    requestBody = StreamUtils.copyToString(inputStream, Charset.forName("utf8"));
                }
                log.info("【LogRequest】 ---> requestURL={}, requestBody={}, requestHeads={}",
                        request.url(), requestBody, request.headers().toMultimap());
            } catch (Exception e) {
                log.warn("okhttpClient定制日志拦截器异常", e);
            }

            long t1 = System.nanoTime();//请求发起的时间
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间

            /**
             * 这里不能直接使用response.body().string()的方式输出日志
             * 因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
             * 个新的response给应用层处理
             **/
            try {
                ResponseBody responseBody = response.peekBody(1024 * 1024);
                log.info("【LogResponse】 <--- requestURL={}, costTime={}毫秒, responseBody={}, responseHeaders={}",
                        request.url(), (t2 - t1) / 1e6d, responseBody.string(), response.headers().toMultimap());
            } catch (Exception e) {
                log.warn("okhttpClient定制日志拦截器异常", e);
            }

            return response;
        }
    }
}
