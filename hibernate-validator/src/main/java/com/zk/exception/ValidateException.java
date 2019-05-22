package com.zk.exception;

public class ValidateException extends RuntimeException {
    public ValidateException () {
        super();
    }

    public ValidateException (String message) {
        super(message);
    }

    public ValidateException (String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateException (Throwable cause) {
        super(cause);
    }

    protected ValidateException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
