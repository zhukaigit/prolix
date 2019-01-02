package com.zk.exception;

public class DecryptionException extends RuntimeException {

    public DecryptionException(String message, Throwable e) {
     super(message, e);
    }
}
