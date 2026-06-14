package com.batalhanaval.view.componentes;

import javax.swing.JButton;

public class BotaoCelula extends JButton {

    private final int linha;
    private final int coluna;

    public BotaoCelula(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
