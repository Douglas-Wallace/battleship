package com.batalhanaval.enums;

//Criando quantos navios podem existir de cada tipo
public enum TipoNavio {
    PORTA_AVIOES(5, 1, "Porta-aviões"),
    CRUZADOR(3, 2, "Cruzador"),
    DESTROYER(2, 3, "Destroyer"),
    SUBMARINO(1, 4, "Submarino");   
    
    private final int tamanho;
    private final int limite;
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
    
    public int getLimite(){
        return limite;
    }
}
