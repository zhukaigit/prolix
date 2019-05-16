package com.zk.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResponse handlerException (Exception e) {
        log.error("handlerException", e);
        return BaseResponse.error(e.getMessage());
    }
}
