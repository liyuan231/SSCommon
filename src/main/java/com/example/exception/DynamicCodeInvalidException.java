package com.example.exception;

public class DynamicCodeInvalidException extends Exception{
    public DynamicCodeInvalidException() {
    }

    public DynamicCodeInvalidException(String message) {
        super(message);
    }

    public DynamicCodeInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamicCodeInvalidException(Throwable cause) {
        super(cause);
    }

    public DynamicCodeInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
