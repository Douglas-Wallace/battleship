package com.batalhanaval.enums;

//Criando quantos navios podem existir de cada tipo
public enum TipoNavio {
    PORTA_AVIAO(5, 1, "Porta Aviao"),
    SUBMARINO(2, 2, "Submarino");
    
    private final int limite;
    private final int tamanho;
    private final String nome;
    
    TipoNavio(int tamanho, int limite, String nome){
        this.tamanho = tamanho;
        this.limite = limite;
        this.nome = nome;
    }

    public int getTamanho() {
        return tamanho;
    }

    public String getNome() {
        return nome;
    }
    //Acessar o limite na hora da validação
    public int getLimite(){
        return limite;
    }
}
