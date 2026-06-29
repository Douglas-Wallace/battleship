package com.batalhanaval.model.entities;

import com.batalhanaval.model.enums.Direcao;
import com.batalhanaval.model.enums.TipoNavio;

public class Jogador {

    private String nome;
    private Tabuleiro tabuleiro;
    private Tabuleiro tabuleiroRastreamento;

    public Jogador(String nome) {
        setNome(nome);
        setTabuleiro();
        setTabuleiroRastreamento();
    }

    // ---- Métodos de negócio ----

    public void posicionarNavio(TipoNavio tipo, int linha, int coluna, Direcao direcao) {
        tabuleiro.adicionarNavio(new Navio(tipo), linha, coluna, direcao);
    }

    public boolean naviosAfundados() {
        return tabuleiro.naviosAfundados();
    }

    public void reiniciar() {
        setTabuleiro();
        setTabuleiroRastreamento();
    }

    // ---- Getters e Setters ----

    public String getNome()                     { return nome; }
    private void setNome(String nome)           { this.nome = nome; }

    public Tabuleiro getTabuleiro()             { return tabuleiro; }
    private void setTabuleiro()                 { this.tabuleiro = new Tabuleiro(); }

    public Tabuleiro getTabuleiroRastreamento() { return tabuleiroRastreamento; }
    private void setTabuleiroRastreamento()     { this.tabuleiroRastreamento = new Tabuleiro(); }
}