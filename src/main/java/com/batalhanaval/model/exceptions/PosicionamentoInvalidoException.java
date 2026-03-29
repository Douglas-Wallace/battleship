package com.batalhanaval.model.exceptions;

public class PosicionamentoInvalidoException extends RuntimeException{
    public PosicionamentoInvalidoException(String mensagem){
        super(mensagem);
    }
}