package com.lagrandee.kinMel.exception;

import com.lagrandee.kinMel.KinMelCustomMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserException {


    @ExceptionHandler
    public ResponseEntity<KinMelCustomMessage> handleException(UserAlreadyExistsException exc) {

        // create a StudentErrorResponse

        KinMelCustomMessage error = new KinMelCustomMessage();

        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        // return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<KinMelCustomMessage> handleException(UnableToSendMailException exc) {
        KinMelCustomMessage error = new KinMelCustomMessage();
        error.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        // return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

}
