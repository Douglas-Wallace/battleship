package com.batalhanaval.view;

import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.enums.StatusCelula;
import com.batalhanaval.model.util.TabuleiroRenderer;

public class ConsoleView {

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void exibirTabuleiro(char[][] visao, int tamanho) {

        for (int i = 0; i <= tamanho; i++) {
            for (int j = 0; j <= tamanho; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("   ");
                } else if (i == 0) {
                    System.out.printf("%2d ", j);
                } else if (j == 0) {
                    System.out.printf("%2c ", (char) ('A' + i - 1));
                } else {
                    System.out.printf("%2c ", visao[i - 1][j - 1]);
                }
            }

            System.out.println();
        }
    }

    public void exibirTabuleiro(Jogador jogador) {
        exibirTabuleiro(
                TabuleiroRenderer.visaoPropria(jogador.getTabuleiro()),
                jogador.getTabuleiro().getTamanho()
        );
    }

    public void exibirTabuleiroComMascara(Jogador jogador) {
        exibirTabuleiro(
                TabuleiroRenderer.visaoInimigo(jogador.getTabuleiro()),
                jogador.getTabuleiro().getTamanho()
        );
    }

    public void mostrarResultadoAtaque(StatusCelula resultado) {
        switch (resultado) {
            case AGUA ->
                System.out.println("Errou!");
            case ACERTOU ->
                System.out.println("Acertou!");
            case AFUNDOU ->
                System.out.println("Afundou um navio!");
            case JA_ATACADO ->
                System.out.println("Você já atacou essa posição.");
        }
    }
}
