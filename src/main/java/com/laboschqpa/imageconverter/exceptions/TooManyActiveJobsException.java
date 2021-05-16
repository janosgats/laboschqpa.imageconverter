package com.laboschqpa.imageconverter.exceptions;

public class TooManyActiveJobsException extends RuntimeException {
    public TooManyActiveJobsException() {
    }

    public TooManyActiveJobsException(String message) {
        super(message);
    }

    public TooManyActiveJobsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyActiveJobsException(Throwable cause) {
        super(cause);
    }

    public TooManyActiveJobsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
