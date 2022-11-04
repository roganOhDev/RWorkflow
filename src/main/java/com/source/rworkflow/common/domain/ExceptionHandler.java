package com.source.rworkflow.common.domain;

import com.source.rworkflow.common.exception.RException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> exception(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> exception(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RException.class)
    public ResponseEntity<ExceptionDto> exception(RException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e));
    }
}
