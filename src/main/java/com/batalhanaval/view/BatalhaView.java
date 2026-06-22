package com.batalhanaval.view;

import com.batalhanaval.model.entities.Celula;
import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.enums.StatusCelula;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.BiConsumer;

/**
 * Tela de batalha — usada por cada cliente no multiplayer.
 *
 * Layout:
 *   [Seu tabuleiro]   [Tabuleiro do inimigo — clicável só no seu turno]
 *
 * Uso:
 *   BatalhaView tela = new BatalhaView(jogador, aoAtacar);
 *   tela.setVisible(true);
 *
 *   // Quando for o turno deste jogador:
 *   tela.habilitarAtaque(true);
 *
 *   // Quando receber resultado de um ataque:
 *   tela.registrarResultadoAtaque(linha, coluna, resultado);
 *
 *   // Quando o inimigo atacar seu tabuleiro:
 *   tela.atualizarTabuleiro();
 *
 *   // Ao fim da partida:
 *   tela.mostrarFimDeJogo("Você venceu!");
 *
 * Callbacks:
 *   aoAtacar: (linha, coluna) → chamado quando o jogador clica para atacar
 */
public class BatalhaView extends JFrame {

    // --- Paleta ---
    private static final Color COR_FUNDO           = new Color(10, 20, 40);
    private static final Color COR_CELULA          = new Color(20, 60, 100);
    private static final Color COR_CELULA_BORDA    = new Color(30, 90, 140);
    private static final Color COR_NAVIO           = new Color(60, 160, 220);
    private static final Color COR_ACERTO          = new Color(220, 80, 60);
    private static final Color COR_AGUA            = new Color(30, 100, 160);
    private static final Color COR_AFUNDADO        = new Color(80, 80, 80);
    private static final Color COR_HOVER           = new Color(255, 220, 60, 120);
    private static final Color COR_BLOQUEADO       = new Color(10, 20, 40, 160);
    private static final Color COR_TEXTO           = new Color(200, 230, 255);
    private static final Color COR_PAINEL          = new Color(15, 35, 65);
    private static final Color COR_DESTAQUE        = new Color(80, 200, 255);
    private static final Color COR_TURNO_ATIVO     = new Color(50, 220, 120);
    private static final Color COR_TURNO_INATIVO   = new Color(180, 60, 60);

    private static final int TAMANHO_CELULA = 46;
    private static final int TAMANHO_HEADER = 28;

    // --- Estado ---
    private final Jogador jogador;
    private final BiConsumer<Integer, Integer> aoAtacar;
    private boolean meuTurno = false;

    private int hoverLinha  = -1;
    private int hoverColuna = -1;

    // --- Componentes ---
    private TabuleiroPanel painelProprio;
    private TabuleiroPanel painelInimigo;
    private JLabel labelTurno;
    private JTextArea areaLog;

    // ---

    /**
     * @param jogador   o jogador dono desta tela
     * @param aoAtacar  callback (linha, coluna) disparado quando o jogador ataca
     */
    public BatalhaView(Jogador jogador, BiConsumer<Integer, Integer> aoAtacar) {
        this.jogador  = jogador;
        this.aoAtacar = aoAtacar;

        configurarJanela();
        construirUI();
    }

    // --- API pública ---

    /** Habilita ou bloqueia o tabuleiro inimigo para ataque. */
    public void habilitarAtaque(boolean habilitado) {
        this.meuTurno = habilitado;
        labelTurno.setText(habilitado ? "⚔  SUA VEZ" : "⏳  AGUARDANDO...");
        labelTurno.setForeground(habilitado ? COR_TURNO_ATIVO : COR_TURNO_INATIVO);
        painelInimigo.repaint();
    }

    /** Registra o resultado de um ataque feito por este jogador no rastreamento. */
    public void registrarResultadoAtaque(int linha, int coluna, StatusCelula resultado) {
        jogador.getTabuleiroRastreamento().registrarAtaque(linha, coluna, resultado);
        painelInimigo.repaint();
        adicionarLog(mensagemResultado(resultado, linha, coluna));
    }

    /** Atualiza o tabuleiro próprio (após receber ataque do inimigo). */
    public void atualizarTabuleiro() {
        painelProprio.repaint();
    }

    /** Mostra mensagem de fim de jogo. */
    public void mostrarFimDeJogo(String mensagem) {
        habilitarAtaque(false);
        JOptionPane.showMessageDialog(this, mensagem, "Fim de jogo", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Adiciona uma linha ao log de mensagens. */
    public void adicionarLog(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append(mensagem + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    // --- Configuração ---

    private void configurarJanela() {
        setTitle("Batalha Naval — " + jogador.getNome());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(12, 12));
    }

    private void construirUI() {
        add(construirCabecalho(), BorderLayout.NORTH);
        add(construirCentro(),    BorderLayout.CENTER);
        add(construirLog(),       BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel construirCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(14, 20, 0, 20));

        JLabel titulo = new JLabel("BATALHA NAVAL — " + jogador.getNome().toUpperCase());
        titulo.setFont(new Font("Monospaced", Font.BOLD, 15));
        titulo.setForeground(COR_DESTAQUE);

        labelTurno = new JLabel("⏳  AGUARDANDO...");
        labelTurno.setFont(new Font("Monospaced", Font.BOLD, 13));
        labelTurno.setForeground(COR_TURNO_INATIVO);

        painel.add(titulo,     BorderLayout.WEST);
        painel.add(labelTurno, BorderLayout.EAST);
        return painel;
    }

    private JPanel construirCentro() {
        JPanel painel = new JPanel(new GridLayout(1, 2, 20, 0));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));

        int tamanho = jogador.getTabuleiro().getTamanho();
        int largura = TAMANHO_HEADER + tamanho * TAMANHO_CELULA + 20;
        int altura  = TAMANHO_HEADER + tamanho * TAMANHO_CELULA + 20;

        painelProprio = new TabuleiroPanel(false);
        painelProprio.setPreferredSize(new Dimension(largura, altura));

        painelInimigo = new TabuleiroPanel(true);
        painelInimigo.setPreferredSize(new Dimension(largura, altura));

        JPanel colEsq = construirColuna("SEU TABULEIRO",    painelProprio);
        JPanel colDir = construirColuna("TABULEIRO INIMIGO", painelInimigo);

        painel.add(colEsq);
        painel.add(colDir);
        return painel;
    }

    private JPanel construirColuna(String titulo, JPanel painel) {
        JPanel col = new JPanel(new BorderLayout(0, 6));
        col.setBackground(COR_FUNDO);

        JLabel label = new JLabel(titulo, SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", Font.BOLD, 11));
        label.setForeground(new Color(100, 150, 200));

        col.add(label, BorderLayout.NORTH);
        col.add(painel, BorderLayout.CENTER);
        return col;
    }

    private JScrollPane construirLog() {
        areaLog = new JTextArea(4, 40);
        areaLog.setEditable(false);
        areaLog.setBackground(COR_PAINEL);
        areaLog.setForeground(COR_TEXTO);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaLog.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(30, 60, 100)));
        scroll.setBackground(COR_PAINEL);
        return scroll;
    }

    // --- Mensagens do log ---

    private String mensagemResultado(StatusCelula resultado, int linha, int coluna) {
        char letra = (char) ('A' + coluna);
        String coord = letra + "" + (linha + 1);
        return switch (resultado) {
            case AGUA     -> "► " + coord + " — Água!";
            case ACERTOU  -> "► " + coord + " — Acertou!";
            case AFUNDOU  -> "► " + coord + " — Navio afundado!";
            default       -> "► " + coord + " — Já atacado.";
        };
    }

    // --- Painel do tabuleiro ---

    private class TabuleiroPanel extends JPanel {

        private final boolean ehInimigo;

        public TabuleiroPanel(boolean ehInimigo) {
            this.ehInimigo = ehInimigo;
            setBackground(COR_FUNDO);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            if (ehInimigo) {
                addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        if (!meuTurno) return;
                        int[] cel = pixelParaCelula(e.getX(), e.getY());
                        hoverLinha  = cel[0];
                        hoverColuna = cel[1];
                        repaint();
                    }
                });

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!meuTurno) return;
                        int[] cel = pixelParaCelula(e.getX(), e.getY());
                        if (cel[0] < 0) return;

                        Celula celula = jogador.getTabuleiroRastreamento().getCelula(cel[0], cel[1]);
                        if (celula.foiAtacada()) {
                            adicionarLog("Posição já atacada, escolha outra.");
                            return;
                        }

                        habilitarAtaque(false); // bloqueia até receber resposta
                        aoAtacar.accept(cel[0], cel[1]);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hoverLinha  = -1;
                        hoverColuna = -1;
                        repaint();
                    }
                });
            }
        }

        private int[] pixelParaCelula(int px, int py) {
            int x = px - TAMANHO_HEADER;
            int y = py - TAMANHO_HEADER;
            int tamanho = jogador.getTabuleiro().getTamanho();
            int col = x / TAMANHO_CELULA;
            int lin = y / TAMANHO_CELULA;
            if (col < 0 || col >= tamanho || lin < 0 || lin >= tamanho) {
                return new int[]{-1, -1};
            }
            return new int[]{lin, col};
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Tabuleiro tab = ehInimigo
                ? jogador.getTabuleiroRastreamento()
                : jogador.getTabuleiro();

            int tamanho = tab.getTamanho();

            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {
                    int x = TAMANHO_HEADER + j * TAMANHO_CELULA;
                    int y = TAMANHO_HEADER + i * TAMANHO_CELULA;

                    Celula celula = tab.getCelula(i, j);
                    Color cor = corCelula(celula, ehInimigo);

                    // Hover no tabuleiro inimigo
                    boolean isHover = ehInimigo && meuTurno
                        && i == hoverLinha && j == hoverColuna
                        && !celula.foiAtacada();

                    g2.setColor(cor);
                    g2.fillRoundRect(x + 1, y + 1, TAMANHO_CELULA - 2, TAMANHO_CELULA - 2, 6, 6);

                    if (isHover) {
                        g2.setColor(COR_HOVER);
                        g2.fillRoundRect(x + 1, y + 1, TAMANHO_CELULA - 2, TAMANHO_CELULA - 2, 6, 6);
                    }

                    g2.setColor(COR_CELULA_BORDA);
                    g2.drawRoundRect(x + 1, y + 1, TAMANHO_CELULA - 2, TAMANHO_CELULA - 2, 6, 6);

                    // Overlay de bloqueio no tabuleiro inimigo quando não é o turno
                    if (ehInimigo && !meuTurno) {
                        g2.setColor(COR_BLOQUEADO);
                        g2.fillRoundRect(x + 1, y + 1, TAMANHO_CELULA - 2, TAMANHO_CELULA - 2, 6, 6);
                    }
                }
            }

            // Headers --- números
            g2.setFont(new Font("Monospaced", Font.BOLD, 11));
            for (int j = 0; j < tamanho; j++) {
                int x = TAMANHO_HEADER + j * TAMANHO_CELULA + TAMANHO_CELULA / 2;
                g2.setColor(COR_DESTAQUE);
                drawCentered(g2, String.valueOf(j + 1), x, TAMANHO_HEADER / 2 + 4);
            }

            // Headers --- letras
            for (int i = 0; i < tamanho; i++) {
                int y = TAMANHO_HEADER + i * TAMANHO_CELULA + TAMANHO_CELULA / 2;
                g2.setColor(COR_DESTAQUE);
                drawCentered(g2, String.valueOf((char) ('A' + i)), TAMANHO_HEADER / 2, y + 4);
            }
        }

        private Color corCelula(Celula celula, boolean ehInimigo) {
            if (!celula.foiAtacada()) {
                return ehInimigo ? COR_CELULA : (celula.temNavio() ? COR_NAVIO : COR_CELULA);
            }

            if (ehInimigo) {
                // Usa o statusAtaque guardado no rastreamento
                StatusCelula status = celula.getStatusAtaque();
                if (status == null) return COR_CELULA;
                return switch (status) {
                    case AGUA    -> COR_AGUA;
                    case ACERTOU -> COR_ACERTO;
                    case AFUNDOU -> COR_AFUNDADO;
                    default      -> COR_CELULA;
                };
            } else {
                // Tabuleiro próprio --- mostra o que aconteceu com seus navios
                if (celula.temNavio()) {
                    return celula.navioAfundado() ? COR_AFUNDADO : COR_ACERTO;
                }
                return COR_AGUA;
            }
        }

        private void drawCentered(Graphics2D g2, String texto, int cx, int cy) {
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto, cx - fm.stringWidth(texto) / 2, cy);
        }
    }
}