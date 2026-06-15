package com.batalhanaval.model.enums;

//Criando quantos navios podem existir de cada tipo
// (quantidade, tamanho, nome)
public enum TipoNavio {
    PORTA_AVIOES(5, 1, "Porta-Aviões"),
    CRUZADOR(4, 2, "Cruzador"),
    CONTRA_TOPEDEIRO(3, 2, "Contra Torpedeiro"),
    SUBMARINO(2, 3, "Submarino");

    private int limite;
    private int tamanho;
    private String nome;

    TipoNavio(int tamanho, int limite, String nome) {
        setTamanho(tamanho);
        setLimite(limite);
        setNome(nome);
    }

    public int getTamanho() { return tamanho; }
    private void setTamanho(int tamanho){ this.tamanho = tamanho; }

    public String getNome() { return nome; }
    private void setNome(String nome){ this.nome = nome; }
    
    public int getLimite() { return limite; }
    private void setLimite(int limite){ this.limite = limite; }
}
