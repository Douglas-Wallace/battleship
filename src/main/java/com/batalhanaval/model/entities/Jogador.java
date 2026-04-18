package com.batalhanaval.model.entities;


import com.batalhanaval.enums.Direcao;
import com.batalhanaval.model.entities.Navio;
import com.batalhanaval.model.entities.Tabuleiro;

public abstract class Jogador {

    protected String nome;
    protected Tabuleiro tabuleiro;

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
    
    public  abstract void posicionarNavios();
    public abstract void atacar(Jogador inimigo);
}