package com.batalhanaval.model.entities;

import com.batalhanaval.enums.Direcao;
import com.batalhanaval.enums.StatusCelula;
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

    public StatusCelula atacar(int linha, int coluna) {

        if (!estaDentroDoTabuleiro(linha, coluna)) {
            throw new PosicionamentoInvalidoException("Tabuleiro - posição invalida");
        }

        Celula celula = tabuleiro[linha][coluna];

        if (celula.foiAtacada()) {
            return StatusCelula.JA_ATACADO;
        }

        if (celula.temNavio()) {
            celula.atacar();
            if (celula.navioAfundado()) {
                return StatusCelula.AFUNDOU;
            } else {
                return StatusCelula.ACERTOU;
            }

        } else {
            celula.atacar();
            return StatusCelula.AGUA;
        }

    }

    public char[][] getVisaoPropria() {
        
        char[][] tabuleiroProprio = new char[tamanho][tamanho];
        
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {

                Celula celula = tabuleiro[i][j];

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
    
    public char[][] getVisaoInimigo() {
        
        char[][] tabuleiroInimigo = new char[tamanho][tamanho];
        
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {

                Celula celula = tabuleiro[i][j];

                if (!celula.foiAtacada()) {
                    tabuleiroInimigo[i][j] = '~';  
                } else if (!celula.temNavio()) {
                    tabuleiroInimigo[i][j] = 'O'; 
                } else if(celula.navioAfundado()) {
                    tabuleiroInimigo[i][j] = '-';  
                } else {
                    tabuleiroInimigo[i][j] = 'X'; 
                }
            }
        }
        
        return tabuleiroInimigo;
    }

    public void adicionarNavio(Navio navio, int linha, int coluna, Direcao direcao) {
        linha = linha - 1;
        coluna = coluna - 1;
        validarDirecao(direcao);

        //validação
        for (int i = 0; i < navio.getTipo().getTamanho(); i++) {
            int pos[] = calcularPosicao(linha, coluna, direcao, i);
            int parteLinha = pos[0];
            int parteColuna = pos[1];

            if (!estaDentroDoTabuleiro(parteLinha, parteColuna) || !podePosicionar(parteLinha, parteColuna)) {
                throw new PosicionamentoInvalidoException("Tabuleiro - posição invalida");
            }

        }

        //inserção
        for (int i = 0; i < navio.getTipo().getTamanho(); i++) {
            int pos[] = calcularPosicao(linha, coluna, direcao, i);
            int parteLinha = pos[0];
            int parteColuna = pos[1];

            tabuleiro[parteLinha][parteColuna].setParte(navio.getPartes().get(i));

        }

    }

    public void validarDirecao(Direcao direcao) {
        if (direcao != Direcao.NORTE && direcao != Direcao.SUL && direcao != Direcao.OESTE && direcao != Direcao.LESTE) {
            throw new RuntimeException("Tabuleiro - Direção inválida");
        }
    }

    private int[] calcularPosicao(int linha, int coluna, Direcao direcao, int i) {
        int novaLinha = linha;
        int novaColuna = coluna;

        switch (direcao) {
            case Direcao.NORTE:
                novaLinha = linha - i;
                break;
            case Direcao.SUL:
                novaLinha = linha + i;
                break;
            case Direcao.LESTE:
                novaColuna = coluna + i;
                break;
            case Direcao.OESTE:
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
    
    public int getTamanho(){
        return tamanho;
    }
}
