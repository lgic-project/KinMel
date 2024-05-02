package com.lagrandee.kinMel.exception;

public class NotInsertedException extends RuntimeException{
    public NotInsertedException(String message) {
        super(message);
    }

    public NotInsertedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotInsertedException(Throwable cause) {
        super(cause);
    }
}
