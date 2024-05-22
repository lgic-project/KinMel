package com.lagrandee.kinMel.exception;

public class UserNotVerified extends RuntimeException{
    public UserNotVerified(String message) {
        super(message);
    }

    public UserNotVerified(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotVerified(Throwable cause) {
        super(cause);
    }

    public UserNotVerified(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
