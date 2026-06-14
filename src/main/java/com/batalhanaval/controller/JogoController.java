package com.batalhanaval.controller;

import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.Jogo;
import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.enums.Direcao;
import com.batalhanaval.model.enums.StatusCelula;
import com.batalhanaval.model.enums.TipoNavio;
import com.batalhanaval.model.exceptions.PosicionamentoInvalidoException;
import com.batalhanaval.model.util.CoordenadaParser;
import com.batalhanaval.network.ClientePartida;
import com.batalhanaval.network.ConexaoPartida;
import com.batalhanaval.network.Mensagem;
import com.batalhanaval.network.ServidorPartida;
import com.batalhanaval.network.TipoMensagem;
import com.batalhanaval.view.BatalhaView;
import com.batalhanaval.view.ConsoleView;
import com.batalhanaval.view.PosicionamentoView;
import com.batalhanaval.view.TabuleiroRenderer;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class JogoController {

    private final Jogo jogo;
    private ConexaoPartida conexao;
    private boolean ehServidor;
    private boolean usarSwing;

    // Views
    private ConsoleView consoleView;
    private BatalhaView batalhaView;

    // Sincronização do posicionamento
    private boolean euTermineiPosicionamento  = false;
    private boolean inimigoProntoParaBatalha  = false;

    private final Scanner sc = new Scanner(System.in);

    public JogoController(Jogo jogo) {
        this.jogo = jogo;
    }

    // ── Início ────────────────────────────────────────────────────────────────

    public void iniciar() {
        escolherModoConexao();
        escolherInterface();
        conectar();
        iniciarPosicionamento();
    }

    // ── Escolhas iniciais ─────────────────────────────────────────────────────

    private void escolherModoConexao() {
        int opcao = JOptionPane.showOptionDialog(
            null,
            "Como deseja jogar?",
            "Batalha Naval",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Criar partida", "Entrar em partida"},
            "Criar partida"
        );
        ehServidor = (opcao == 0);
    }

    private void escolherInterface() {
        int opcao = JOptionPane.showOptionDialog(
            null,
            "Escolha a interface:",
            "Batalha Naval",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Console", "Gráfico"},
            "Gráfico"
        );
        usarSwing = (opcao == 1);

        if (!usarSwing) {
            consoleView = new ConsoleView();
        }
    }

    // ── Conexão ───────────────────────────────────────────────────────────────

    private void conectar() {
        try {
            if (ehServidor) {
                conexao = new ServidorPartida();
                mostrarMensagem("Aguardando jogador...");
            } else {
                String ip = JOptionPane.showInputDialog(null,
                    "Digite o IP do servidor:",
                    "Entrar em partida",
                    JOptionPane.QUESTION_MESSAGE);
                if (ip == null || ip.isBlank()) {
                    mostrarMensagem("IP inválido.");
                    System.exit(0);
                }
                conexao = new ClientePartida(ip.trim());
                mostrarMensagem("Conectando ao servidor...");
            }

            conexao.conectar();
            mostrarMensagem("Conectado! Posicione seus navios.");

        } catch (IOException e) {
            mostrarMensagem("Erro de conexão: " + e.getMessage());
            System.exit(0);
        }
    }

    // ── Posicionamento ────────────────────────────────────────────────────────

    private void iniciarPosicionamento() {
        if (usarSwing) {
            SwingUtilities.invokeLater(() -> {
                Jogador jogador = jogo.getJogadorAtual();
                new PosicionamentoView(jogador, () -> aoTerminarPosicionamento()).setVisible(true);
            });
        } else {
            posicionarNaviosConsole();
            aoTerminarPosicionamento();
        }
    }

    private void posicionarNaviosConsole() {
        Jogador jogador = jogo.getJogadorAtual();
        Tabuleiro tab   = jogador.getTabuleiro();

        for (TipoNavio tipo : TipoNavio.values()) {
            for (int i = 0; i < tipo.getLimite(); i++) {
                boolean posicionado = false;

                while (!posicionado) {
                    try {
                        consoleView.mostrarMensagem("\nPosicione um " + tipo.getNome());
                        consoleView.exibirTabuleiro(TabuleiroRenderer.visaoPropria(tab), tab.getTamanho());

                        int[] coord     = lerCoordenada(jogador);
                        Direcao direcao = lerDirecao();

                        jogador.posicionarNavio(tipo, coord[0], coord[1], direcao);
                        posicionado = true;

                    } catch (PosicionamentoInvalidoException e) {
                        consoleView.mostrarMensagem("Posição inválida. Tente novamente.");
                    } catch (Exception e) {
                        consoleView.mostrarMensagem("Erro: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Chamado quando este jogador termina o posicionamento.
     * Envia INICIO para o inimigo e aguarda o INICIO dele em uma Thread.
     */
    private void aoTerminarPosicionamento() {
        euTermineiPosicionamento = true;
        mostrarMensagem("Navios posicionados! Aguardando adversário...");

        try {
            conexao.enviar(new Mensagem(TipoMensagem.INICIO));
        } catch (IOException e) {
            mostrarMensagem("Erro ao avisar adversário: " + e.getMessage());
            return;
        }

        // Thread que aguarda o INICIO do inimigo
        new Thread(() -> {
            try {
                Mensagem msg = conexao.receber();
                if (msg.getTipo() == TipoMensagem.INICIO) {
                    inimigoProntoParaBatalha = true;
                    ambosProtos();
                }
            } catch (IOException e) {
                mostrarMensagem("Adversário desconectou durante posicionamento.");
            }
        }).start();
    }

    /**
     * Chamado quando os dois jogadores terminaram o posicionamento.
     * O servidor decide quem começa e inicia a batalha.
     */
    private void ambosProtos() {
        if (usarSwing) {
            SwingUtilities.invokeLater(() -> abrirBatalhaSwing());
        } else {
            iniciarBatalhaConsole();
        }
    }

    // ── Batalha Swing ─────────────────────────────────────────────────────────

    private void abrirBatalhaSwing() {
        Jogador jogador = jogo.getJogadorAtual();

        batalhaView = new BatalhaView(jogador, (linha, coluna) -> {
            // Callback: jogador clicou para atacar
            try {
                conexao.enviar(new Mensagem(TipoMensagem.ATAQUE, linha, coluna));
            } catch (IOException e) {
                batalhaView.adicionarLog("Erro ao enviar ataque.");
            }
        });

        // Servidor começa, cliente aguarda
        if (ehServidor) {
            batalhaView.habilitarAtaque(true);
            batalhaView.adicionarLog("Sua vez! Você começa.");
        } else {
            batalhaView.habilitarAtaque(false);
            batalhaView.adicionarLog("Aguardando adversário atacar...");
        }

        batalhaView.setVisible(true);

        // Thread que fica ouvindo mensagens da rede
        new Thread(() -> ouvirRedeSwing()).start();
    }

    private void ouvirRedeSwing() {
        try {
            while (true) {
                Mensagem msg = conexao.receber();

                switch (msg.getTipo()) {

                    case ATAQUE -> {
                        // Inimigo atacou meu tabuleiro
                        StatusCelula resultado = jogo.getJogadorAtual()
                            .getTabuleiro().atacar(msg.getLinha(), msg.getColuna());

                        // Responde com o resultado
                        conexao.enviar(new Mensagem(TipoMensagem.RESULTADO, resultado));

                        // Atualiza meu tabuleiro na tela
                        SwingUtilities.invokeLater(() -> {
                            batalhaView.atualizarTabuleiro();

                            if (jogo.getJogadorAtual().naviosAfundados()) {
                                batalhaView.mostrarFimDeJogo("Você perdeu!");
                                conexao.encerrar();
                            } else {
                                // Minha vez de atacar
                                batalhaView.habilitarAtaque(true);
                                batalhaView.adicionarLog("Sua vez!");
                            }
                        });
                    }

                    case RESULTADO -> {
                        // Recebi o resultado do meu ataque
                        StatusCelula resultado = msg.getResultado();

                        // Pega a última coordenada enviada — guardada temporariamente
                        int linha   = msg.getLinha()   != null ? msg.getLinha()   : 0;
                        int coluna  = msg.getColuna()  != null ? msg.getColuna()  : 0;

                        jogo.getJogadorAtual().getTabuleiroRastreamento()
                            .registrarAtaque(linha, coluna, resultado);

                        SwingUtilities.invokeLater(() -> {
                            batalhaView.registrarResultadoAtaque(linha, coluna, resultado);

                            if (resultado == StatusCelula.AFUNDOU
                                    && jogo.getJogadorInimigo().naviosAfundados()) {
                                batalhaView.mostrarFimDeJogo("Você venceu!");
                                conexao.encerrar();
                            }
                            // Turno passa para o inimigo — aguarda ATAQUE dele
                            batalhaView.habilitarAtaque(false);
                            batalhaView.adicionarLog("Aguardando adversário...");
                        });
                    }

                    case DESCONECTADO -> {
                        SwingUtilities.invokeLater(() ->
                            batalhaView.mostrarFimDeJogo("Adversário desconectou."));
                        return;
                    }
                }
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() ->
                batalhaView.adicionarLog("Conexão perdida: " + e.getMessage()));
        }
    }

    // ── Batalha Console ───────────────────────────────────────────────────────

    private void iniciarBatalhaConsole() {
        consoleView.mostrarMensagem("\n=== BATALHA INICIADA ===");

        boolean meuTurno = ehServidor; // servidor começa

        // Thread que ouve a rede no console
        new Thread(() -> ouvirRedeConsole()).start();

        if (meuTurno) {
            realizarAtaqueConsole();
        } else {
            consoleView.mostrarMensagem("Aguardando adversário atacar...");
        }
    }

    private void ouvirRedeConsole() {
        try {
            while (true) {
                Mensagem msg = conexao.receber();

                switch (msg.getTipo()) {

                    case ATAQUE -> {
                        StatusCelula resultado = jogo.getJogadorAtual()
                            .getTabuleiro().atacar(msg.getLinha(), msg.getColuna());

                        conexao.enviar(new Mensagem(TipoMensagem.RESULTADO, resultado));

                        consoleView.mostrarResultadoAtaque(resultado);
                        exibirTabuleirosProprios();

                        if (jogo.getJogadorAtual().naviosAfundados()) {
                            consoleView.mostrarMensagem("Você perdeu!");
                            conexao.encerrar();
                            return;
                        }

                        consoleView.mostrarMensagem("\nSua vez!");
                        realizarAtaqueConsole();
                    }

                    case RESULTADO -> {
                        StatusCelula resultado = msg.getResultado();
                        consoleView.mostrarResultadoAtaque(resultado);

                        if (resultado == StatusCelula.AFUNDOU
                                && jogo.getJogadorInimigo().naviosAfundados()) {
                            consoleView.mostrarMensagem("Você venceu!");
                            conexao.encerrar();
                            return;
                        }

                        consoleView.mostrarMensagem("Aguardando adversário atacar...");
                    }

                    case DESCONECTADO -> {
                        consoleView.mostrarMensagem("Adversário desconectou.");
                        return;
                    }
                }
            }
        } catch (IOException e) {
            consoleView.mostrarMensagem("Conexão perdida: " + e.getMessage());
        }
    }

    private void realizarAtaqueConsole() {
        boolean atacou = false;

        while (!atacou) {
            try {
                exibirTabuleirosProprios();
                int[] coord = lerCoordenada(jogo.getJogadorAtual());
                conexao.enviar(new Mensagem(TipoMensagem.ATAQUE, coord[0], coord[1]));
                atacou = true;
            } catch (IOException e) {
                consoleView.mostrarMensagem("Erro ao enviar ataque: " + e.getMessage());
            } catch (Exception e) {
                consoleView.mostrarMensagem("Entrada inválida. Tente novamente.");
            }
        }
    }

    private void exibirTabuleirosProprios() {
        Tabuleiro tab = jogo.getJogadorAtual().getTabuleiro();
        Tabuleiro ras = jogo.getJogadorAtual().getTabuleiroRastreamento();

        consoleView.mostrarMensagem("\nSeu tabuleiro:");
        consoleView.exibirTabuleiro(TabuleiroRenderer.visaoPropria(tab), tab.getTamanho());

        consoleView.mostrarMensagem("\nTabuleiro inimigo:");
        consoleView.exibirTabuleiro(TabuleiroRenderer.visaoRastreamento(ras), ras.getTamanho());
    }

    // ── Leitura de entrada (console) ──────────────────────────────────────────

    private int[] lerCoordenada(Jogador jogador) {
        consoleView.mostrarMensagem("Digite a coordenada (ex: B5): ");
        String entrada = sc.next().toUpperCase();

        if (entrada.length() < 2) throw new IllegalArgumentException("Coordenada inválida");

        int[] coord   = CoordenadaParser.parse(entrada);
        int tamanho   = jogador.getTabuleiro().getTamanho();

        if (coord[0] < 0 || coord[0] >= tamanho || coord[1] < 0 || coord[1] >= tamanho) {
            throw new IllegalArgumentException("Fora do tabuleiro");
        }
        return coord;
    }

    private Direcao lerDirecao() {
        consoleView.mostrarMensagem("Digite a direcao (N/S/L/O): ");
        String entrada = sc.next().toUpperCase();

        return switch (entrada) {
            case "N" -> Direcao.NORTE;
            case "S" -> Direcao.SUL;
            case "L" -> Direcao.LESTE;
            case "O" -> Direcao.OESTE;
            default  -> throw new IllegalArgumentException("Direção inválida");
        };
    }

    // ── Utilitário ────────────────────────────────────────────────────────────

    private void mostrarMensagem(String msg) {
        if (usarSwing) {
            if (batalhaView != null) {
                batalhaView.adicionarLog(msg);
            } else {
                JOptionPane.showMessageDialog(null, msg);
            }
        } else {
            consoleView.mostrarMensagem(msg);
        }
    }
}