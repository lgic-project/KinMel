package com.lagrandee.kinMel.exception;

public class UnableToSendMailException extends RuntimeException{
    public UnableToSendMailException(String message) {
        super(message);
    }

    public UnableToSendMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToSendMailException(Throwable cause) {
        super(cause);
    }
}
