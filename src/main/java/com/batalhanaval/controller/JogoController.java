package com.batalhanaval.controller;

import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.Jogo;
import com.batalhanaval.model.enums.Direcao;
import com.batalhanaval.model.enums.StatusCelula;
import com.batalhanaval.model.enums.TipoNavio;
import com.batalhanaval.model.exceptions.PosicionamentoInvalidoException;
import com.batalhanaval.view.ConsoleView;
import java.util.Scanner;

public class JogoController {

    private final Jogo jogo;
    private final ConsoleView view;
    private final Scanner sc;

    public JogoController(Jogo jogo) {
        this.jogo = jogo;
        this.view = new ConsoleView();
        this.sc = new Scanner(System.in);
    }

    public void iniciar() {
        posicionarNavios(jogo.getJogadorAtual());
        jogo.trocarTurno();
        posicionarNavios(jogo.getJogadorAtual());
        jogo.trocarTurno();

        while (!jogo.terminou()) {
            Jogador atual = jogo.getJogadorAtual();
            Jogador inimigo = jogo.getJogadorInimigo();

            view.mostrarMensagem("\n----------------");
            view.mostrarMensagem("Vez de: " + atual.getNome());

            view.mostrarMensagem("\nSeu tabuleiro:");
            view.exibirTabuleiro(atual);

            view.mostrarMensagem("\nTabuleiro inimigo:");
            view.exibirTabuleiroComMascara(inimigo);

            realizarAtaque(atual, inimigo);

            if (jogo.terminou()) {
                view.mostrarMensagem("\nVencedor: " + atual.getNome());
                break;
            }

            jogo.trocarTurno();
        }
    }

    private void posicionarNavios(Jogador jogador) {
        for (TipoNavio tipo : TipoNavio.values()) {
            for (int i = 0; i < tipo.getLimite(); i++) {
                boolean posicionado = false;

                while (!posicionado) {
                    try {
                        view.mostrarMensagem("\nPosicione um " + tipo.getNome());
                        view.exibirTabuleiro(jogador);

                        int[] coord = lerCoordenada(jogador);
                        Direcao direcao = lerDirecao();

                        jogador.posicionarNavio(tipo, coord[0], coord[1], direcao);
                        posicionado = true;

                    } catch (PosicionamentoInvalidoException e) {
                        view.mostrarMensagem("Posição inválida. Tente novamente.");
                    } catch (Exception e) {
                        view.mostrarMensagem("Entrada inválida. Tente novamente.");
                    }
                }
            }
        }
    }

    private void realizarAtaque(Jogador atacante, Jogador inimigo) {
        StatusCelula resultado;

        do {
            try {
                int[] coord = lerCoordenada(atacante);
                resultado = atacante.atacar(inimigo, coord[0], coord[1]);
                view.mostrarResultadoAtaque(resultado);
            } catch (PosicionamentoInvalidoException e) {
                view.mostrarMensagem("Posição inválida. Tente novamente.");
                resultado = null;
            } catch (Exception e) {
                view.mostrarMensagem("Entrada inválida. Tente novamente.");
                resultado = null;
            }
        } while (resultado == null || resultado == StatusCelula.JA_ATACADO);
    }

    private int[] lerCoordenada(Jogador jogador) {
        view.mostrarMensagem("Digite a coordenada (ex: B5): ");
        String entrada = sc.next().toUpperCase();

        if (entrada.length() < 2) {
            throw new IllegalArgumentException("Coordenada inválida");
        }

        char colunaChar = entrada.charAt(0);
        int coluna = colunaChar - 'A';
        int linha = Integer.parseInt(entrada.substring(1)) - 1;

        int tamanho = jogador.getTabuleiro().getTamanho();

        if (linha < 0 || linha >= tamanho || coluna < 0 || coluna >= tamanho) {
            throw new IllegalArgumentException("Fora do tabuleiro");
        }

        return new int[]{linha, coluna};
    }

    private Direcao lerDirecao() {
        view.mostrarMensagem("Digite a direção (N/S/L/O): ");
        String entrada = sc.next().toUpperCase();

        return switch (entrada) {
            case "N" -> Direcao.NORTE;
            case "S" -> Direcao.SUL;
            case "L" -> Direcao.LESTE;
            case "O" -> Direcao.OESTE;
            default -> throw new IllegalArgumentException("Direção inválida");
        };
    }
}