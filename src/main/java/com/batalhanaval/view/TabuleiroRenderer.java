package com.batalhanaval.view;

import com.batalhanaval.model.entities.Celula;
import com.batalhanaval.model.entities.Tabuleiro;

public class TabuleiroRenderer {

    public static char[][] visaoPropria(Tabuleiro tabuleiro) {
        int tamanho = tabuleiro.getTamanho();
        char[][] tabuleiroProprio = new char[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {

                Celula celula = tabuleiro.getCelula(i, j);

                if (celula.temNavio()) {
                    if (celula.navioAfundado()) {
                        tabuleiroProprio[i][j] = '-';
                    } else if (celula.getParte().foiAtingida()) {
                        tabuleiroProprio[i][j] = 'X';
                    } else {
                        tabuleiroProprio[i][j] = 'N';
                    }
                } else if (celula.foiAtacada()) {
                    tabuleiroProprio[i][j] = 'O';
                } else {
                    tabuleiroProprio[i][j] = '~';
                }
            }
        }
        return tabuleiroProprio;
    }

    public static char[][] visaoInimigo(Tabuleiro tabuleiro) {
        int tamanho = tabuleiro.getTamanho();
        char[][] tabuleiroInimigo = new char[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {

                Celula celula = tabuleiro.getCelula(i, j);

                if (!celula.foiAtacada()) {
                    tabuleiroInimigo[i][j] = '~';
                } else if (!celula.temNavio()) {
                    tabuleiroInimigo[i][j] = 'O';
                } else if (celula.navioAfundado()) {
                    tabuleiroInimigo[i][j] = '-';
                } else {
                    tabuleiroInimigo[i][j] = 'X';
                }
            }
        }
        return tabuleiroInimigo;
    }
}
