package com.batalhanaval.model.entities;

public abstract class Jogador {

    protected Tabuleiro tabuleiro;
    protected String nome;

    public Jogador(String nome) {
        this.nome = nome;

        this.tabuleiro = new Tabuleiro();
    }

    public abstract void jogar(Jogador inimigo);

    public abstract void exibirTabuleiro();

    public void posicionarNavio(Navio navio, int linha, int coluna, char direcao) {
        tabuleiro.adicionarNavio(navio, linha, coluna, direcao);

    }

    public void exibirTabuleiroIndividual() {
        tabuleiro.exibirProprio();
    }

    public void exibirTabuleiroInimigo(Jogador inimigo) {
        inimigo.tabuleiro.exibirInimigo();
    }

    public boolean naviosAfundados() {
        return tabuleiro.naviosAfundados();
    }

}
