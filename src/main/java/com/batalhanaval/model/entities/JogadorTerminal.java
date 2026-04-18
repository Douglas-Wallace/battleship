package com.batalhanaval.model.entities;

import com.batalhanaval.enums.Direcao;
import com.batalhanaval.enums.StatusCelula;
import com.batalhanaval.enums.TipoNavio;
import com.batalhanaval.model.exceptions.PosicionamentoInvalidoException;
import java.util.Scanner;

public class JogadorTerminal extends Jogador {

    private final Scanner sc = new Scanner(System.in);

    public JogadorTerminal(String nome) {
        super(nome);
    }

    @Override
    public void posicionarNavios() {
        for (TipoNavio tipo : TipoNavio.values()) {
            for (int i = 0; i < tipo.getLimite(); i++) {

                boolean posicionado = false;

                while (!posicionado) {
                    try {
                        System.out.println("\nPosicione um " + tipo.getNome() + "   tamanho("+tipo.getTamanho()+")");
                        exibirTabuleiro();

                        int[] coordenada = lerCoordenada();
                        int linha = coordenada[0];
                        int coluna = coordenada[1];

                        Direcao direcao = lerDirecao();

                        Navio navio = new Navio(tipo);
                        tabuleiro.adicionarNavio(navio, linha, coluna, direcao);

                        System.out.println(tipo.getNome() + " posicionado com sucesso.");
                        exibirTabuleiro();
                        posicionado = true;

                    } catch (PosicionamentoInvalidoException e) {
                        System.out.println("Posição inválida. Tente novamente.");
                    } catch (NumberFormatException e) {
                        System.out.println("Linha inválida. Use algo como B5.");
                    } catch (Exception e) {
                        System.out.println("Entrada inválida. Tente novamente.");
                    }
                }
            }
        }
    }

    @Override
    public void atacar(Jogador inimigo) {
        StatusCelula resultado;

        do {
            try {
                int[] coordenada = lerCoordenada();
                int linha = coordenada[0];
                int coluna = coordenada[1];

                resultado = inimigo.getTabuleiro().atacar(linha, coluna);

                if (resultado == StatusCelula.JA_ATACADO) {
                    System.out.println("Você já atacou essa posição.");
                } else {
                    System.out.println("Resultado: " + resultado);
                }

            } catch (PosicionamentoInvalidoException e) {
                System.out.println("Posição inválida. Tente novamente.");
                resultado = null;
            } catch (Exception e) {
                System.out.println("Entrada inválida. Tente novamente.");
                resultado = null;
            }
        } while (resultado == StatusCelula.JA_ATACADO || resultado == null);
    }

    private int[] lerCoordenada() {
        System.out.print("Digite a coordenada (ex: B5): ");
        String entrada = sc.next().toUpperCase();

        if (entrada.length() < 2) {
            throw new IllegalArgumentException("Coordenada inválida");
        }

        char colunaChar = entrada.charAt(0);
        int coluna = colunaChar - 'A';
        int linha = Integer.parseInt(entrada.substring(1)) - 1;

        if (linha < 0 || linha >= tabuleiro.getTamanho()
                || coluna < 0 || coluna >= tabuleiro.getTamanho()) {
            throw new IllegalArgumentException("Coordenada fora do tabuleiro");
        }

        return new int[]{linha, coluna};
    }

    private Direcao lerDirecao() {
        System.out.print("Digite a direção (N/S/L/O): ");
        String dir = sc.next().toUpperCase();

        switch (dir) {
            case "N":
                return Direcao.NORTE;
            case "S":
                return Direcao.SUL;
            case "L":
                return Direcao.LESTE;
            case "O":
                return Direcao.OESTE;
            default:
                throw new IllegalArgumentException("Direção inválida");
        }
    }
    
    private void imprimirVisao(char[][] visao) {
        int tamanho = tabuleiro.getTamanho();

        for (int i = 0; i <= tamanho; i++) {
            for (int j = 0; j <= tamanho; j++) {

                if (i == 0 && j == 0) {
                    System.out.print("   ");
                } else if (i == 0) {
                    System.out.print((char) ('A' + j - 1) + " ");
                } else if (j == 0) {
                    System.out.printf("%2d ", i);
                } else {
                    System.out.print(visao[i - 1][j - 1] + " ");
                }
            }
            System.out.println();
        }
    }

    public void exibirTabuleiro() {
        imprimirVisao(tabuleiro.getVisaoPropria());
    }

    public void exibirTabuleiroInimigo(Jogador inimigo) {
        imprimirVisao(inimigo.getTabuleiro().getVisaoInimigo());
    }
}
