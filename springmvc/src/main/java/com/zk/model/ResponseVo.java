package com.zk.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseVo<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> ResponseVo success(T data) {
        ResponseVo<T> tResponseVo = new ResponseVo<>();
        tResponseVo.code = 200;
        tResponseVo.msg = "success";
        tResponseVo.data = data;
        return tResponseVo;
    }

    public static <T> ResponseVo error(T data) {
        ResponseVo<T> tResponseVo = new ResponseVo<>();
        tResponseVo.code = 500;
        tResponseVo.msg = "error";
        tResponseVo.data = data;
        return tResponseVo;
    }
}
