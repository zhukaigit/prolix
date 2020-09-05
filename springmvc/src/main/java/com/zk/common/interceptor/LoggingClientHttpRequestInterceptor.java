package com.zk.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.Charset;

/**
 * restTemplate日志拦截器
 * 用途：打印请求报文及、返回报文等相关信息
 */
@Slf4j
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    // 不打印请求报文的线程。若get()有值表示不打印请求报文
    public static final ThreadLocal NO_LOG_REQUEST_BODY = new ThreadLocal();
    // 不打印返回报文的线程。若get()有值表示不打印返回报文
    public static final ThreadLocal NO_LOG_RESPONSE_BODY = new ThreadLocal();

    @Override
    public ClientHttpResponse intercept (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // 打印请求报文日志
        logRequest(request, body);
        // 请求开始时间
        long start = System.nanoTime();
        // 远程调用
        ClientHttpResponse response = execution.execute(request, body);
        // 请求结束时间
        long end = System.nanoTime();
        // 转换response
        response = new CustomClientHttpResponse(response);
        // 打印返回结果日志
        logResponse(request, response, end - start);
        return response;
    }

    private void logRequest (HttpRequest request, byte[] body) throws IOException {
        Object o = NO_LOG_REQUEST_BODY.get();
        String requestBody = o == null ? new String(body, "UTF-8") : "不打印请求报文";
        if (o != null) {
            NO_LOG_REQUEST_BODY.remove();
        }
        log.info("【restTemplate LogRequest】 ---> 【requestURL】={}, 【requestMethod】={}, 【requestHeads】={}, 【requestBody】={}",
                request.getURI(), request.getMethod(), request.getHeaders(), requestBody);
    }

    private void logResponse (HttpRequest request, ClientHttpResponse response, long costTime) throws IOException {
        Object o = NO_LOG_RESPONSE_BODY.get();
        String responseBody = o == null ? new String(CustomClientHttpResponse.copyToByteArray(response.getBody()), Charset
                .forName("utf-8")) : "不打印返回报文";
        if (o != null) {
            NO_LOG_RESPONSE_BODY.remove();
        }
        log.info("【restTemplate LogResponse】 <--- 【requestURL】={}, 【statusCode】={}, 【costTime】={}毫秒, 【responseHeaders】={}, 【responseBody】={}",
                request.getURI(), response.getStatusCode(), costTime / 1e6d, response.getHeaders(), responseBody);
    }

    /**
     * 方便将ClientHttpResponse中的返回报文转成byte数组，以便多次重复读取返回报文
     */
    private static class CustomClientHttpResponse extends AbstractClientHttpResponse {
        private ClientHttpResponse clientHttpResponse;
        private byte[] body;

        public CustomClientHttpResponse () {
            // 无参数构造
        }

        public CustomClientHttpResponse (ClientHttpResponse clientHttpResponse) {
            InputStream responseBody = null;
            try {
                responseBody = clientHttpResponse.getBody();
            } catch (IOException e) {
                log.error("读取response body异常", e);
                throw new RuntimeException("读取response body异常");
            }
            try {

                body = copyToByteArray(responseBody);
            } catch (IOException e) {
                log.error("将流转换成字节", e);
                throw new RuntimeException("将流转换成字节");
            }
            this.clientHttpResponse = clientHttpResponse;
        }

        @Override
        public int getRawStatusCode () throws IOException {
            return clientHttpResponse.getRawStatusCode();
        }

        @Override
        public String getStatusText () throws IOException {
            return clientHttpResponse.getStatusText();
        }

        @Override
        public void close () {
            clientHttpResponse.close();
        }

        @Override
        public InputStream getBody () throws IOException {
            return new ByteArrayInputStream(body);
        }

        @Override
        public HttpHeaders getHeaders () {
            return clientHttpResponse.getHeaders();
        }

        // =================== 工具类方法 ===================

        static byte[] copyToByteArray (InputStream in) throws IOException {
            if (in == null) {
                return new byte[0];
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
                copy(in, out);
                return out.toByteArray();
            }
        }

        protected static int copy (InputStream in, OutputStream out) throws IOException {
            Assert.notNull(in, "No InputStream specified");
            Assert.notNull(out, "No OutputStream specified");

            int byteCount = 0;
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        }
    }


}
