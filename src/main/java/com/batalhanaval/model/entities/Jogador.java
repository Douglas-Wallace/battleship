package com.batalhanaval.model.entities;

public abstract class Jogador {

    protected Tabuleiro tabuleiro;
    protected String nome;

    public Jogador(String nome) {
        this.nome = nome;

        this.tabuleiro = new Tabuleiro();
    }

    public abstract void jogar();

    public abstract void exibirTabuleiro();

    public void posicionarNavio(Navio navio, int linha, int coluna, char direcao) {
        tabuleiro.adicionarNavio(navio, linha, coluna, direcao);

    }

    public void exibirTabuleiroIndividual() {
        tabuleiro.getVisaoPropria();
    }
    
    public void exibirTabuleiroInimigo(Jogador inimigo){
        inimigo.tabuleiro.getVisaoInimigo();
    }

}
