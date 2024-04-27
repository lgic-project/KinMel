package com.lagrandee.kinMel.exception;

import com.lagrandee.kinMel.KinMelCustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserException {


    @ExceptionHandler
    public ResponseEntity<KinMelCustomException> handleException(UserAlreadyExistsException exc) {

        // create a StudentErrorResponse

        KinMelCustomException error = new KinMelCustomException();

        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        // return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
