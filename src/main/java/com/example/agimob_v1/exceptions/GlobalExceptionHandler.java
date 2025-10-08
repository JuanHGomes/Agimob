package com.example.agimob_v1.exceptions;

import com.example.agimob_v1.model.Usuario;
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

        body.put("timespam", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_ACCEPTABLE);
        body.put("erro", "Valor inserido inválido!");
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ValorMenorIgualZeroException.class)
    private ResponseEntity<Object> valorMenorIgualZeroExceptionHandler(ValorMenorIgualZeroException exception){

        Map<String, Object> body = new HashMap<>();

        body.put("timespam", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_ACCEPTABLE);
        body.put("erro", "Valor menor ou igual a zero!!");
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ValorForaDaFaixaException.class)
    private ResponseEntity<Object> valorForaDaFaixaHandlerr(ValorForaDaFaixaException exception){

        Map<String, Object> body = new HashMap<>();

        body.put("timespam", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_ACCEPTABLE);
        body.put("erro", "Valor fora da faixa aceitável!");
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(SimulacaoNaoEncontradaException.class)
    private ResponseEntity<Object> simulacaoNaoEncontradaExceptionHandler(SimulacaoNaoEncontradaException exception){

        Map<String, Object> body = new HashMap<>();

        body.put("timespam", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_ACCEPTABLE);
        body.put("erro", "Simulação não encontrada!");
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    private ResponseEntity<Object> usuarioNaoEncontratoExceptionHandler(UsuarioNaoEncontradoException exception){

        Map<String, Object> body = new HashMap<>();

        body.put("timespam", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_ACCEPTABLE);
        body.put("erro", "Usuário não encontrado!");
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }


}
