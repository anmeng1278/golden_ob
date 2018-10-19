package com.jsj.member.ob.exception;

/**
 * 系统致命异常
 */
public class FatalException extends RuntimeException {



    public FatalException() {
    }

    public FatalException(String message) {
        super(message);
    }

    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalException(Throwable cause) {
        super(cause);
    }

}