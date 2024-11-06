package com.crudops.skylark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InfosExceptionHandler {

    @ExceptionHandler(value = {InfosNotFoundException.class})
    public ResponseEntity<Object> handleInfosNotFoundException(InfosNotFoundException infosNotFoundException){

        InfosException infosException = new InfosException(infosNotFoundException.getMessage(), infosNotFoundException.getCause(), HttpStatus.NOT_FOUND  );

        return new ResponseEntity<>(infosException , HttpStatus.NOT_FOUND);
    }
}
