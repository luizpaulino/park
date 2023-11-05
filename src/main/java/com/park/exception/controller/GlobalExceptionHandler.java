package com.park.exception.controller;

import com.park.exception.CacheException;
import com.park.exception.CondutorExistenteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CacheException.class)
    public ResponseEntity<String> handleCahceException(Exception e) {
        return new ResponseEntity<>("Erro ao tratar o cache", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CondutorExistenteException.class)
    public ResponseEntity<String> handleCondutorExistenteException(CondutorExistenteException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
