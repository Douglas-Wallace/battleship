package com.batalhanaval.model.entities;

import com.batalhanaval.model.entities.Jogador;

public class Jogo {
    private Jogador jogadorAtual;
    private Jogador jogadorInimigo;

    public Jogo(Jogador jogador1, Jogador jogador2) {
        this.jogadorAtual = jogador1;
        this.jogadorInimigo = jogador2;
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public Jogador getJogadorInimigo() {
        return jogadorInimigo;
    }

    public void trocarTurno() {
        Jogador temp = jogadorAtual;
        jogadorAtual = jogadorInimigo;
        jogadorInimigo = temp;
    }

    public boolean terminou() {
        return jogadorInimigo.naviosAfundados();
    }
}