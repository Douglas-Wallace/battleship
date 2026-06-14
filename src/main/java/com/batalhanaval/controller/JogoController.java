package com.batalhanaval.controller;

import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.Jogo;
import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.enums.Direcao;
import com.batalhanaval.model.enums.StatusCelula;
import com.batalhanaval.model.enums.TipoNavio;
import com.batalhanaval.model.exceptions.PosicionamentoInvalidoException;
import com.batalhanaval.model.util.CoordenadaParser;
import com.batalhanaval.view.BatalhaView;
import com.batalhanaval.view.ConsoleView;
import com.batalhanaval.view.PosicionamentoView;
import com.batalhanaval.view.TabuleiroRenderer;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class JogoController {

    private final Jogo jogo;
    private final ConsoleView view;
    private final Scanner sc;
    private BatalhaView batalhaView;

    public JogoController(Jogo jogo) {
        this.jogo = jogo;
        this.view = new ConsoleView();
        this.sc   = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao = JOptionPane.showOptionDialog(
            null,
            "Como deseja jogar?",
            "Batalha Naval",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Console", "Interface Gráfica"},
            "Interface Gráfica"
        );

        if (opcao == 1) {
            iniciarSwing();
        } else {
            fasePosicionamento();
            faseBatalha();
        }
    }

    // ── Swing ─────────────────────────────────────────────────────────────────

    private void iniciarSwing() {
        SwingUtilities.invokeLater(() -> {
            Jogador jogador1 = jogo.getJogadorAtual();
            Jogador jogador2 = jogo.getJogadorInimigo();

            new PosicionamentoView(jogador1, () ->
                new PosicionamentoView(jogador2, () ->
                    iniciarBatalhaSwing()
                ).setVisible(true)
            ).setVisible(true);
        });
    }

    private void iniciarBatalhaSwing() {
        SwingUtilities.invokeLater(() -> {
            batalhaView = new BatalhaView(jogo.getJogadorAtual(), (linha, coluna) -> {
                StatusCelula resultado = jogo.getJogadorAtual()
                    .atacar(jogo.getJogadorInimigo(), linha, coluna);

                batalhaView.registrarResultadoAtaque(linha, coluna, resultado);

                if (jogo.terminou()) {
                    batalhaView.mostrarFimDeJogo("Você venceu!");
                } else {
                    jogo.trocarTurno();
                    batalhaView.habilitarAtaque(false);
                    // futuro: aqui o ataque será enviado pela rede
                }
            });

            batalhaView.habilitarAtaque(true);
            batalhaView.setVisible(true);
        });
    }

    // ── Console ───────────────────────────────────────────────────────────────

    private void fasePosicionamento() {
        posicionarNavios(jogo.getJogadorAtual());
        jogo.trocarTurno();
        posicionarNavios(jogo.getJogadorAtual());
        jogo.trocarTurno();
    }

    private void posicionarNavios(Jogador jogador) {
        Tabuleiro tab = jogador.getTabuleiro();

        for (TipoNavio tipo : TipoNavio.values()) {
            for (int i = 0; i < tipo.getLimite(); i++) {
                boolean posicionado = false;

                while (!posicionado) {
                    try {
                        view.mostrarMensagem("Vez de: " + jogador.getNome());
                        view.mostrarMensagem("\nPosicione um " + tipo.getNome());
                        view.exibirTabuleiro(TabuleiroRenderer.visaoPropria(tab), tab.getTamanho());

                        int[] coord = lerCoordenada(jogador);
                        Direcao direcao = lerDirecao();

                        jogador.posicionarNavio(tipo, coord[0], coord[1], direcao);
                        posicionado = true;

                    } catch (PosicionamentoInvalidoException e) {
                        view.mostrarMensagem("Posição inválida. Tente novamente.");
                    } catch (Exception e) {
                        view.mostrarMensagem("Erro: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void faseBatalha() {
        while (!jogo.terminou()) {
            Jogador atual   = jogo.getJogadorAtual();
            Jogador inimigo = jogo.getJogadorInimigo();

            executarTurno(atual, inimigo);

            if (jogo.terminou()) {
                view.mostrarMensagem("\nVencedor: " + atual.getNome());
                break;
            }

            jogo.trocarTurno();
        }
    }

    private void executarTurno(Jogador atual, Jogador inimigo) {
        Tabuleiro tabAtual        = atual.getTabuleiro();
        Tabuleiro tabRastreamento = atual.getTabuleiroRastreamento();

        view.mostrarMensagem("\n----------------");
        view.mostrarMensagem("Vez de: " + atual.getNome());

        view.mostrarMensagem("\nSeu tabuleiro:");
        view.exibirTabuleiro(TabuleiroRenderer.visaoPropria(tabAtual), tabAtual.getTamanho());

        view.mostrarMensagem("\nTabuleiro inimigo:");
        view.exibirTabuleiro(TabuleiroRenderer.visaoRastreamento(tabRastreamento), tabRastreamento.getTamanho());

        realizarAtaque(atual, inimigo);
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

    // ── Leitura de entrada ────────────────────────────────────────────────────

    private int[] lerCoordenada(Jogador jogador) {
        view.mostrarMensagem("Digite a coordenada (ex: B5): ");
        String entrada = sc.next().toUpperCase();

        if (entrada.length() < 2) {
            throw new IllegalArgumentException("Coordenada inválida");
        }

        int[] coord   = CoordenadaParser.parse(entrada);
        int tamanho   = jogador.getTabuleiro().getTamanho();

        if (coord[0] < 0 || coord[0] >= tamanho || coord[1] < 0 || coord[1] >= tamanho) {
            throw new IllegalArgumentException("Fora do tabuleiro");
        }

        return coord;
    }

    private Direcao lerDirecao() {
        view.mostrarMensagem("Digite a direcao (N/S/L/O): ");
        String entrada = sc.next().toUpperCase();

        return switch (entrada) {
            case "N" -> Direcao.NORTE;
            case "S" -> Direcao.SUL;
            case "L" -> Direcao.LESTE;
            case "O" -> Direcao.OESTE;
            default  -> throw new IllegalArgumentException("Direção inválida");
        };
    }
}