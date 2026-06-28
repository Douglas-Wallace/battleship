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

    private Jogo jogo;
    private ConexaoPartida conexao;
    private boolean ehServidor;
    private boolean usarSwing;

    // Views
    private ConsoleView consoleView;
    private BatalhaView batalhaView;

    // Sincronização do posicionamento
    private boolean euTermineiPosicionamento = false;
    private boolean inimigoProntoParaBatalha = false;

    private final Scanner sc = new Scanner(System.in);

    public JogoController(Jogo jogo) {
        setJogoController(jogo);
    }

    // ---- Início ----

    public void iniciar() {
        // Reseta o jogador para nova partida
        jogo.getJogador().reiniciar();
        setEuTermineiPosicionamento(false);
        setInimigoProntoParaBatalha(false);
        batalhaView = null;

        escolherModoConexao();
        escolherInterface();
        conectar();
        iniciarPosicionamento();
    }
    
    // ---- Escolhas iniciais ----

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
            setConsoleView();
        }
    }

    // ---- Conexão ----

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

    // ---- Posicionamento ----

    private void iniciarPosicionamento() {
        if (usarSwing) {
            SwingUtilities.invokeLater(() ->
                new PosicionamentoView(jogo.getJogador(), () -> aoTerminarPosicionamento())
                    .setVisible(true)
            );
        } else {
            posicionarNaviosConsole();
            aoTerminarPosicionamento();
        }
    }

    private void posicionarNaviosConsole() {
        Jogador jogador = jogo.getJogador();
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

    private void aoTerminarPosicionamento() {
        euTermineiPosicionamento = true;
        mostrarMensagem("Navios posicionados! Aguardando adversário...");

        try {
            conexao.enviar(new Mensagem(TipoMensagem.INICIO));
        } catch (IOException e) {
            mostrarMensagem("Erro ao avisar adversário: " + e.getMessage());
            return;
        }

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

    private void ambosProtos() {
        if (usarSwing) {
            SwingUtilities.invokeLater(() -> abrirBatalhaSwing());
        } else {
            iniciarBatalhaConsole();
        }
    }

    // ---- Fim de jogo ----

    private void encerrarPartida(boolean venceu) {
        String mensagem = venceu ? "Você venceu!" : "Você perdeu!";

        if (usarSwing) {
            SwingUtilities.invokeLater(() -> {
                if (batalhaView != null) batalhaView.dispose();
                JOptionPane.showMessageDialog(null, mensagem, "Fim de jogo",
                    JOptionPane.INFORMATION_MESSAGE);
                reiniciar();
            });
        } else {
            consoleView.mostrarMensagem("\n" + mensagem);
            reiniciar();
        }
    }

    private void reiniciar() {
        conexao.encerrar();
        iniciar(); // volta pro menu inicial
    }

    // ---- Batalha Swing ----

    private void abrirBatalhaSwing() {
        Jogador jogador = jogo.getJogador();

        batalhaView = new BatalhaView(jogador, (linha, coluna) -> {
            try {
                conexao.enviar(new Mensagem(TipoMensagem.ATAQUE, linha, coluna));
            } catch (IOException e) {
                batalhaView.adicionarLog("Erro ao enviar ataque.");
            }
        });

        if (ehServidor) {
            batalhaView.habilitarAtaque(true);
            batalhaView.adicionarLog("Sua vez! Você começa.");
        } else {
            batalhaView.habilitarAtaque(false);
            batalhaView.adicionarLog("Aguardando adversário atacar...");
        }

        batalhaView.setVisible(true);

        new Thread(() -> ouvirRedeSwing()).start();
    }

    private void ouvirRedeSwing() {
        try {
            while (true) {
                Mensagem msg = conexao.receber();

                switch (msg.getTipo()) {

                    case ATAQUE -> {
                        StatusCelula resultado = jogo.getJogador()
                            .getTabuleiro().atacar(msg.getLinha(), msg.getColuna());

                        conexao.enviar(new Mensagem(TipoMensagem.RESULTADO, resultado,
                            msg.getLinha(), msg.getColuna()));

                        SwingUtilities.invokeLater(() -> batalhaView.atualizarTabuleiro());

                        if (jogo.getJogador().naviosAfundados()) {
                            conexao.enviar(new Mensagem(TipoMensagem.DERROTA));
                            encerrarPartida(false);
                            return;
                        }

                        SwingUtilities.invokeLater(() -> {
                            batalhaView.habilitarAtaque(true);
                            batalhaView.adicionarLog("Sua vez!");
                        });
                    }

                    case RESULTADO -> {
                        StatusCelula resultado = msg.getResultado();
                        int linha  = msg.getLinha();
                        int coluna = msg.getColuna();

                        jogo.getJogador().getTabuleiroRastreamento()
                            .registrarAtaque(linha, coluna, resultado);

                        SwingUtilities.invokeLater(() -> {
                            batalhaView.registrarResultadoAtaque(linha, coluna, resultado);
                            batalhaView.habilitarAtaque(false);
                            batalhaView.adicionarLog("Aguardando adversário...");
                        });
                    }

                    case DERROTA -> {
                        // inimigo avisou que perdeu — eu venci
                        encerrarPartida(true);
                        return;
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

    // ---- Batalha Console ----

    private void iniciarBatalhaConsole() {
        consoleView.mostrarMensagem("\n=== BATALHA INICIADA ===");

        new Thread(() -> ouvirRedeConsole()).start();

        if (ehServidor) {
            consoleView.mostrarMensagem("Você começa!");
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
                        StatusCelula resultado = jogo.getJogador()
                            .getTabuleiro().atacar(msg.getLinha(), msg.getColuna());

                        conexao.enviar(new Mensagem(TipoMensagem.RESULTADO, resultado,
                            msg.getLinha(), msg.getColuna()));

                        consoleView.mostrarResultadoAtaque(resultado);
                        exibirTabuleirosProprios();

                        if (jogo.getJogador().naviosAfundados()) {
                            conexao.enviar(new Mensagem(TipoMensagem.DERROTA));
                            encerrarPartida(false);
                            return;
                        }

                        consoleView.mostrarMensagem("\nSua vez!");
                        realizarAtaqueConsole();
                    }

                    case RESULTADO -> {
                        StatusCelula resultado = msg.getResultado();
                        int linha  = msg.getLinha();
                        int coluna = msg.getColuna();

                        jogo.getJogador().getTabuleiroRastreamento()
                            .registrarAtaque(linha, coluna, resultado);

                        consoleView.mostrarResultadoAtaque(resultado);
                        consoleView.mostrarMensagem("Aguardando adversário atacar...");
                    }

                    case DERROTA -> {
                        encerrarPartida(true);
                        return;
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
                int[] coord = lerCoordenada(jogo.getJogador());
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
        Tabuleiro tab = jogo.getJogador().getTabuleiro();
        Tabuleiro ras = jogo.getJogador().getTabuleiroRastreamento();

        consoleView.mostrarMensagem("\nSeu tabuleiro:");
        consoleView.exibirTabuleiro(TabuleiroRenderer.visaoPropria(tab), tab.getTamanho());

        consoleView.mostrarMensagem("\nTabuleiro inimigo:");
        consoleView.exibirTabuleiro(TabuleiroRenderer.visaoRastreamento(ras), ras.getTamanho());
    }

    // ---- Leitura de entrada (console) ----

    private int[] lerCoordenada(Jogador jogador) {
        consoleView.mostrarMensagem("Digite a coordenada (ex: B5): ");
        String entrada = sc.next().toUpperCase();

        if (entrada.length() < 2) throw new IllegalArgumentException("Coordenada inválida");

        int[] coord = CoordenadaParser.parse(entrada);
        int tamanho = jogador.getTabuleiro().getTamanho();

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

    // ---- Utilitário ----

    private void mostrarMensagem(String msg) {
        if (usarSwing) {
            if (batalhaView != null) {
                batalhaView.adicionarLog(msg);
            } else {
                JOptionPane.showMessageDialog(null, msg);
            }
        } else {
            if (consoleView != null) {
                consoleView.mostrarMensagem(msg);
            } else {
                System.out.println(msg);
            }
        }
    }
    
   // ---- Getters e Setters ----
    
    private void setJogoController(Jogo jogo){ this.jogo = jogo; }
    private void setConsoleView() { this.consoleView = new ConsoleView(); }
    private void setEuTermineiPosicionamento(Boolean bool) { this.euTermineiPosicionamento = bool; }
    private void setInimigoProntoParaBatalha(Boolean bool) { this.inimigoProntoParaBatalha = bool; }
    
    
}