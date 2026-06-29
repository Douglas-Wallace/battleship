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

    public static char[][] visaoRastreamento(Tabuleiro tabuleiro) {
        int tamanho = tabuleiro.getTamanho();
        char[][] grid = new char[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                Celula celula = tabuleiro.getCelula(i, j);

                if (!celula.foiAtacada()) {
                    grid[i][j] = '~';
                } else {
                    grid[i][j] = switch (celula.getStatusAtaque()) {
                        case AGUA    -> 'O';
                        case ACERTOU -> 'X';
                        case AFUNDOU -> '-';
                        default      -> '?';
                    };
                }
            }
        }
        return grid;
    }
}