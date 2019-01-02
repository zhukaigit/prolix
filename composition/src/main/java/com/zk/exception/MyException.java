package com.zk.exception;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/14.
 */
public class MyException extends RuntimeException {
    private static final int DEFAULT_CODE = 400;
    private int code;
    private String message;

    public MyException(String message) {
        super(message);
        this.message = message;
        this.code = DEFAULT_CODE;

    }

    public MyException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}