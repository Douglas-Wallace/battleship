package com.batalhanaval.model.entities;

import com.batalhanaval.model.enums.Direcao;
import com.batalhanaval.model.enums.StatusCelula;
import com.batalhanaval.model.exceptions.PosicionamentoInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {

    private final int tamanho = 10;
    private Celula[][] tabuleiro;

    public Tabuleiro() {
        setTabuleiro();
    }

    // ---- Métodos de negócio ----

    public void adicionarNavio(Navio navio, int linha, int coluna, Direcao direcao) {
        validarDirecao(direcao);

        for (int i = 0; i < navio.getTipo().getTamanho(); i++) {
            int[] pos = calcularPosicao(linha, coluna, direcao, i);
            if (!estaDentroDoTabuleiro(pos[0], pos[1]) || !podePosicionar(pos[0], pos[1])) {
                throw new PosicionamentoInvalidoException("Posição invalida");
            }
        }

        for (int i = 0; i < navio.getTipo().getTamanho(); i++) {
            int[] pos = calcularPosicao(linha, coluna, direcao, i);
            tabuleiro[pos[0]][pos[1]].setParte(navio.getPartes().get(i));
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

        celula.atacar();
        
        if (celula.temNavio()) {
            return celula.navioAfundado() ? StatusCelula.AFUNDOU : StatusCelula.ACERTOU;
        }
        return StatusCelula.AGUA;
    }

    public void registrarAtaque(int linha, int coluna, StatusCelula resultado) {
        tabuleiro[linha][coluna].registrarResultado(resultado);
    }

    public boolean naviosAfundados() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                Celula celula = tabuleiro[i][j];
                if (celula.temNavio() && !celula.navioAfundado()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calcula as posições que um navio ocuparia a partir de (linha, coluna)
     * na direção e tamanho informados. Não valida, só calcula.
     */
    public List<int[]> calcularPosicoes(int linha, int coluna, Direcao direcao, int tamanhoNavio) {
        List<int[]> posicoes = new ArrayList<>();
        for (int i = 0; i < tamanhoNavio; i++) {
            posicoes.add(calcularPosicao(linha, coluna, direcao, i));
        }
        return posicoes;
    }

    /**
     * Verifica se um navio pode ser posicionado a partir de (linha, coluna)
     * na direção e tamanho informados.
     */
    public boolean posicionamentoValido(int linha, int coluna, Direcao direcao, int tamanhoNavio) {
        List<int[]> posicoes = calcularPosicoes(linha, coluna, direcao, tamanhoNavio);
        for (int[] pos : posicoes) {
            if (!estaDentroDoTabuleiro(pos[0], pos[1])) return false;
            if (!podePosicionar(pos[0], pos[1]))        return false;
        }
        return true;
    }

    // ---- Métodos auxiliares ----

    private void validarDirecao(Direcao direcao) {
        if (direcao == null) {
            throw new PosicionamentoInvalidoException("Direção inválida");
        }
    }

    private int[] calcularPosicao(int linha, int coluna, Direcao direcao, int i) {
        return switch (direcao) {
            case NORTE -> new int[]{linha - i, coluna};
            case SUL   -> new int[]{linha + i, coluna};
            case LESTE -> new int[]{linha, coluna + i};
            case OESTE -> new int[]{linha, coluna - i};
        };
    }

    public boolean podePosicionar(int linha, int coluna) {
        for (int i = linha - 1; i <= linha + 1; i++) {
            for (int j = coluna - 1; j <= coluna + 1; j++) {
                if (estaDentroDoTabuleiro(i, j) && tabuleiro[i][j].temNavio()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean estaDentroDoTabuleiro(int linha, int coluna) {
        return linha >= 0 && linha < tamanho && coluna >= 0 && coluna < tamanho;
    }

    // ---- Getters e Setters ----

    private void setTabuleiro() {
        this.tabuleiro = new Celula[tamanho][tamanho];
        for (int linha = 0; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho; coluna++) {
                tabuleiro[linha][coluna] = new Celula();
            }
        }
    }

    public int getTamanho() { return tamanho; }

    public Celula getCelula(int linha, int coluna) { return tabuleiro[linha][coluna]; }
}