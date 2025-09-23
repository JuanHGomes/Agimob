package com.example.agimob_v1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(FormatoIndevidoException.class)
    private ResponseEntity<Object> FormatoIndevidoExceptionHadnler(FormatoIndevidoException exception){

        Map<String, Object> body = new HashMap<>();

        body.put("timestam", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_ACCEPTABLE);
        body.put("erro", "Valor inserido inválido!");
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }


}
