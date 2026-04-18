package com.batalhanaval.model.entities;

import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.JogadorTerminal;

public class Jogo {

    private Jogador jogadorAtual;
    private Jogador jogadorInimigo;

    public Jogo(Jogador jogador1, Jogador jogador2) {
        this.jogadorAtual = jogador1;
        this.jogadorInimigo = jogador2;
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n----------------");
            System.out.println("Vez de: " + jogadorAtual.getNome());

            if (jogadorAtual instanceof JogadorTerminal) {
                JogadorTerminal jt = (JogadorTerminal) jogadorAtual;

                System.out.println("\nSeu tabuleiro:");
                jt.exibirTabuleiro();

                System.out.println("\nTabuleiro inimigo:");
                jt.exibirTabuleiroInimigo(jogadorInimigo);
            }

            jogadorAtual.atacar(jogadorInimigo);

            if (verificarVitoria()) {
                System.out.println("\nVencedor: " + jogadorAtual.getNome());
                break;
            }

            trocarTurno();
        }
    }

    private void trocarTurno() {
        Jogador temp = jogadorAtual;
        jogadorAtual = jogadorInimigo;
        jogadorInimigo = temp;
    }

    private boolean verificarVitoria() {
        return jogadorInimigo.naviosAfundados();
    }
}