package com.crudops.skylark.exception;

import com.crudops.skylark.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InfosExceptionHandler {

    @ExceptionHandler(InfosNotFoundException.class)
    public ResponseEntity<Object> handleInfosNotFoundException(InfosNotFoundException exception) {
        return ResponseHandler.responseBuilder(
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                null
        );
    }



    @ExceptionHandler(InfosValidationException.class)
    public ResponseEntity<Object> handleInfosValidationException(InfosValidationException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (exception.getMessage() != null && exception.getMessage().toLowerCase().contains("duplicate")) {
            status = HttpStatus.CONFLICT;
        }

        return ResponseHandler.responseBuilder(
                exception.getMessage(),
                status,
                null
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception exception) {
        return ResponseHandler.responseBuilder(
                "An unexpected error occurred: " + exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        );
    }
}
