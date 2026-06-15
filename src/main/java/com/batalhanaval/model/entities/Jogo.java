package com.batalhanaval.model.entities;

public class Jogo {

    private final Jogador jogador;

    public Jogo(Jogador jogador) {
        this.jogador = jogador;
    }

    public Jogador getJogador() {
        return jogador;
    }
}