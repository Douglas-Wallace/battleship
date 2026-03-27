package com.batalhanaval.model.entities;

public abstract class Jogador {
    protected Tabuleiro tabuleiro;
    protected String nome;

    public Jogador(String nome) {
        this.nome = nome;
        
        this.tabuleiro = new Tabuleiro();
        tabuleiro.iniciar();
    }
    
    
    
    public abstract void jogar();
    public abstract void exibirTabuleiro();
    
    public void posicionarNavio(Navio navio){
        tabuleiro.adicionarNavio(navio);
    }
 
}