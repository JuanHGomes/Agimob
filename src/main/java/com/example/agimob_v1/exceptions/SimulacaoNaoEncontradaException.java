package com.example.agimob_v1.exceptions;

public class SimulacaoNaoEncontradaException extends RuntimeException {
    public SimulacaoNaoEncontradaException(String message) {
        super(message);
    }
    public SimulacaoNaoEncontradaException(){super("Nehuma simulação com esse ID foi localizada, verifique o ID enviado");}
}
