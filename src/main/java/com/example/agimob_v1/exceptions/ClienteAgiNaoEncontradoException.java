package com.example.agimob_v1.exceptions;

public class ClienteAgiNaoEncontradoException extends RuntimeException{
    public ClienteAgiNaoEncontradoException(String message) {super(message);}
    public ClienteAgiNaoEncontradoException() {super("Cliente Agibank não encontrado.");}
}
