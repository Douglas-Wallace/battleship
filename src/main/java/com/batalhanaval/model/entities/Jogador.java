package com.batalhanaval.model.entities;

import com.batalhanaval.enums.Direcao;

public abstract class Jogador {

    protected Tabuleiro tabuleiro;
    protected String nome;

    public Jogador(String nome) {
        this.nome = nome;

        this.tabuleiro = new Tabuleiro();
    }

    public abstract void jogar(Jogador inimigo);
    public abstract void exibirTabuleiroIndividual();
    public abstract void exibirTabuleiroInimigo(Jogador inimigo);

    public void posicionarNavio(Navio navio, int linha, int coluna, Direcao direcao) {
        tabuleiro.adicionarNavio(navio, linha, coluna, direcao);

    }
       
    public boolean naviosAfundados() {
        return tabuleiro.naviosAfundados();
    }
    
    public String getNome(){
        return nome;
    }

}
