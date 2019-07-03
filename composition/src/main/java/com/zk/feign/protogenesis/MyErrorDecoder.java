package com.zk.feign.protogenesis;

import com.zk.exception.MyException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/14.
 */
@Slf4j
public class MyErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = null;
        try {
            if (response.body() != null && response.body().asInputStream() != null) {
                body = StreamUtils.copyToString(
                        response.body().asInputStream(), Charset.forName("utf8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.error("methodKey = {}, body = {}", methodKey, body);
        return new MyException(response.status(), body);
    }
}
