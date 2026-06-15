package com.batalhanaval.model.entities;

public class Jogo {

    private final Jogador jogador;

    public Jogo(Jogador jogador) {
        this.jogador = jogador;
    }
    
    // ---- Getters ----
    public Jogador getJogador() { return jogador; }
}