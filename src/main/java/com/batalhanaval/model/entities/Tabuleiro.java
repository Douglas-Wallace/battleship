package com.batalhanaval.model.entities;

import com.batalhanaval.model.exceptions.PosicionamentoInvalidoException;

public class Tabuleiro {

    private final int tamanho = 10;
    private Celula[][] tabuleiro;

    public Tabuleiro() {
        tabuleiro = new Celula[tamanho][tamanho];

        for (int linha = 0; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho; coluna++) {
                tabuleiro[linha][coluna] = new Celula();
            }
        }
    }

    public String atacar(int linha, int coluna) {
        linha = linha - 1;
        coluna = coluna - 1;

        if (!estaDentroDoTabuleiro(linha, coluna)) {
            throw new PosicionamentoInvalidoException("Tabuleiro - posição invalida");
        }

        Celula celula = tabuleiro[linha][coluna];

        if (celula.foiAtacada()) {
            return "JA_ATACADO";
        }

        if (celula.temNavio()) {
            celula.atacar();
            if (celula.navioAfundado()) {
                return "AFUNDOU";
            } else {
                return "ACERTOU";
            }

        } else {
            celula.atacar();
            return "AGUA";
        }

    }

    public void exibirProprio() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {

                Celula celula = tabuleiro[i][j];

                if (celula.temNavio()) {
                    if (celula.navioAfundado()) {
                        System.out.print("- ");
                    } else if (celula.getParte().foiAtingida()) {
                        System.out.print("X ");
                    } else {
                        System.out.print("N ");
                    }
                } else if (celula.foiAtacada()) {
                    System.out.print("O ");
                } else {
                    System.out.print("~ ");
                }
            }
            System.out.println();
        }
    }

    public void adicionarNavio(Navio navio, int linha, int coluna, char direcao) {
        linha = linha - 1;
        coluna = coluna - 1;
        validarDirecao(direcao);

        //validação
        for (int i = 0; i < navio.getTamanho(); i++) {
            int pos[] = calcularPosicao(linha, coluna, direcao, i);
            int parteLinha = pos[0];
            int parteColuna = pos[1];

            if (!estaDentroDoTabuleiro(parteLinha, parteColuna) || !podePosicionar(parteLinha, parteColuna)) {
                throw new PosicionamentoInvalidoException("Tabuleiro - posição invalida");
            }

        }

        //inserção
        for (int i = 0; i < navio.getTamanho(); i++) {
            int pos[] = calcularPosicao(linha, coluna, direcao, i);
            int parteLinha = pos[0];
            int parteColuna = pos[1];

            tabuleiro[parteLinha][parteColuna].setParte(navio.getPartes().get(i));

        }

    }

    public void validarDirecao(char direcao) {
        if (direcao != 'N' && direcao != 'S' && direcao != 'O' && direcao != 'L') {
            throw new RuntimeException("Tabuleiro - Direção inválida");
        }
    }

    private int[] calcularPosicao(int linha, int coluna, char direcao, int i) {
        int novaLinha = linha;
        int novaColuna = coluna;

        switch (direcao) {
            case 'N':
                novaLinha = linha - i;
                break;
            case 'S':
                novaLinha = linha + i;
                break;
            case 'L':
                novaColuna = coluna + i;
                break;
            case 'O':
                novaColuna = coluna - i;
                break;
        }

        return new int[]{novaLinha, novaColuna};
    }

    public boolean estaDentroDoTabuleiro(int linha, int coluna) {
        if (linha < 0 || linha >= tamanho || coluna < 0 || coluna >= tamanho) {
            return false;
        }
        return true;
    }

    public boolean podePosicionar(int linha, int coluna) {
        for (int i = linha - 1; i <= linha + 1; i++) {
            for (int j = coluna - 1; j <= coluna + 1; j++) {
                if (estaDentroDoTabuleiro(i, j)) {
                    if (tabuleiro[i][j].temNavio()) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    //Exibir tabuleiro inimigo
    public void exibirInimigo() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {

                Celula celula = tabuleiro[i][j];

                if (celula.foiAtacada()) {

                    if (celula.temNavio()) {
                        if (celula.navioAfundado()) {
                            System.out.print("- ");
                        } else {
                            System.out.print("X ");
                        }
                    } else {
                        System.out.print("O ");
                    }

                } else {
                    System.out.print("~ ");
                }
            }
            System.out.println();
        }
    }
    
    public boolean naviosAfundados() {
        for(int i = 0; i < tamanho; i++){
            for(int j = 0; j < tamanho; j++){
                
                Celula celula = tabuleiro[i][j];
                
                if(celula.temNavio() && !celula.navioAfundado()) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
