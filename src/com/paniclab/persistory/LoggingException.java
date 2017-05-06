package com.paniclab.persistory;

/**
 * Created by Сергей on 29.04.2017.
 */
public class LoggingException extends RuntimeException {
    public LoggingException() {
    }

    public LoggingException(String message) {
        super(message);
    }

    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggingException(Throwable cause) {
        super(cause);
    }
}
