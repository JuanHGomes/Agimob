package com.example.agimob_v1.exceptions;

public class FormatoIndevidoException extends RuntimeException{
    public FormatoIndevidoException(String message){
        super(message);
    }

    public FormatoIndevidoException(){
        super("Formato isnerido não suportado!");
    }
}
