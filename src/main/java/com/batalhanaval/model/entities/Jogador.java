package com.batalhanaval.model.entities;

import com.batalhanaval.model.enums.Direcao;
import com.batalhanaval.model.entities.Navio;
import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.enums.StatusCelula;
import com.batalhanaval.model.enums.TipoNavio;

public class Jogador {

    private final String nome;
    private final Tabuleiro tabuleiro;

    public Jogador(String nome) {
        this.nome = nome;
        this.tabuleiro = new Tabuleiro();
    }

    public String getNome() {
        return nome;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public boolean naviosAfundados() {
        return tabuleiro.naviosAfundados();
    }

    public StatusCelula atacar(Jogador inimigo, int linha, int coluna) {
        return inimigo.getTabuleiro().atacar(linha, coluna);
    }

    public void posicionarNavio(TipoNavio tipo, int linha, int coluna, Direcao direcao) {
        tabuleiro.adicionarNavio(new Navio(tipo), linha, coluna, direcao);
    }
}
