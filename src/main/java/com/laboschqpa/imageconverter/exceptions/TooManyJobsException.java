package com.laboschqpa.imageconverter.exceptions;

public class TooManyJobsException extends RuntimeException {
    public TooManyJobsException() {
    }

    public TooManyJobsException(String message) {
        super(message);
    }

    public TooManyJobsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyJobsException(Throwable cause) {
        super(cause);
    }

    public TooManyJobsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
