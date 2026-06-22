package com.batalhanaval.view;

import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.enums.Direcao;
import com.batalhanaval.model.enums.TipoNavio;
import com.batalhanaval.model.exceptions.PosicionamentoInvalidoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela de posicionamento de navios (Swing).
 *
 * Uso:
 *   PosicionamentoView tela = new PosicionamentoView(jogador, () -> proximaFase());
 *   tela.setVisible(true);
 *
 * Controles:
 *   - Hover  → preview das células que o navio vai ocupar (verde = ok, vermelho = inválido)
 *   - Clique → confirma posicionamento
 *   - R      → rotaciona a direção (LESTE → SUL → OESTE → NORTE → LESTE)
 */
public class PosicionamentoView extends JFrame {

    // --- Paleta ---
    private static final Color COR_FUNDO        = new Color(10, 20, 40);
    private static final Color COR_CELULA       = new Color(20, 60, 100);
    private static final Color COR_CELULA_BORDA = new Color(30, 90, 140);
    private static final Color COR_NAVIO        = new Color(60, 160, 220);
    private static final Color COR_PREVIEW_OK   = new Color(50, 200, 120, 180);
    private static final Color COR_PREVIEW_ERRO = new Color(220, 60, 60, 180);
    private static final Color COR_TEXTO        = new Color(200, 230, 255);
    private static final Color COR_PAINEL       = new Color(15, 35, 65);
    private static final Color COR_DESTAQUE     = new Color(80, 200, 255);

    private static final int TAMANHO_CELULA = 50;
    private static final int TAMANHO_HEADER = 30;

    // --- Estado ---
    private final Jogador jogador;
    private final Runnable aoTerminar;

    private final TipoNavio[] filaNavios;
    private int indiceAtual = 0;
    private Direcao direcaoAtual = Direcao.LESTE;

    private int hoverLinha  = -1;
    private int hoverColuna = -1;

    // --- Componentes v
    private TabuleiroPanel painelTabuleiro;
    private JLabel labelNavio;
    private JLabel labelDirecao;
    private JLabel labelInstrucao;
    private JPanel painelFila;

    // ---

    public PosicionamentoView(Jogador jogador, Runnable aoTerminar) {
        this.jogador     = jogador;
        this.aoTerminar  = aoTerminar;
        this.filaNavios  = construirFila();

        configurarJanela();
        construirUI();
        atualizarPainel();

        painelTabuleiro.setFocusable(true);
        painelTabuleiro.requestFocusInWindow();
    }

    // --- Fila de navios ---

    private TipoNavio[] construirFila() {
        List<TipoNavio> fila = new ArrayList<>();
        for (TipoNavio tipo : TipoNavio.values()) {
            for (int i = 0; i < tipo.getLimite(); i++) {
                fila.add(tipo);
            }
        }
        return fila.toArray(new TipoNavio[0]);
    }

    // --- Configuração ---

    private void configurarJanela() {
        setTitle("Batalha Naval — Posicionamento: " + jogador.getNome());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(16, 16));
    }

    private void construirUI() {
        JLabel titulo = new JLabel("POSICIONE SEUS NAVIOS — " + jogador.getNome().toUpperCase());
        titulo.setFont(new Font("Monospaced", Font.BOLD, 15));
        titulo.setForeground(COR_DESTAQUE);
        titulo.setBorder(BorderFactory.createEmptyBorder(16, 20, 0, 0));
        add(titulo, BorderLayout.NORTH);

        painelTabuleiro = new TabuleiroPanel();
        add(painelTabuleiro, BorderLayout.CENTER);

        add(construirPainelLateral(), BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel construirPainelLateral() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(COR_PAINEL);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));
        painel.setPreferredSize(new Dimension(220, 0));

        JLabel tituloNavio   = criarLabel("POSICIONANDO", 11, COR_TEXTO);
        labelNavio           = criarLabel("", 16, COR_DESTAQUE);
        labelNavio.setFont(new Font("Monospaced", Font.BOLD, 16));

        JLabel tituloDirecao = criarLabel("DIREÇÃO", 11, COR_TEXTO);
        labelDirecao         = criarLabel("", 14, COR_NAVIO);
        labelInstrucao       = criarLabel("[ R ] rotacionar", 11, new Color(120, 160, 200));

        JLabel tituloFila    = criarLabel("NAVIOS RESTANTES", 11, COR_TEXTO);
        painelFila = new JPanel();
        painelFila.setLayout(new BoxLayout(painelFila, BoxLayout.Y_AXIS));
        painelFila.setBackground(COR_PAINEL);

        painel.add(tituloNavio);
        painel.add(Box.createVerticalStrut(4));
        painel.add(labelNavio);
        painel.add(Box.createVerticalStrut(16));
        painel.add(tituloDirecao);
        painel.add(Box.createVerticalStrut(4));
        painel.add(labelDirecao);
        painel.add(Box.createVerticalStrut(4));
        painel.add(labelInstrucao);
        painel.add(Box.createVerticalStrut(24));
        painel.add(tituloFila);
        painel.add(Box.createVerticalStrut(8));
        painel.add(painelFila);
        painel.add(Box.createVerticalGlue());
        painel.add(construirLegenda());

        return painel;
    }

    private JPanel construirLegenda() {
        JPanel p = new JPanel(new GridLayout(3, 1, 2, 4));
        p.setBackground(COR_PAINEL);
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(40, 80, 120)),
            "Legenda", 0, 0,
            new Font("Monospaced", Font.PLAIN, 10),
            new Color(100, 140, 180)
        ));
        p.add(itemLegenda(COR_NAVIO,        "Navio posicionado"));
        p.add(itemLegenda(COR_PREVIEW_OK,   "Preview válido"));
        p.add(itemLegenda(COR_PREVIEW_ERRO, "Preview inválido"));
        return p;
    }

    private JPanel itemLegenda(Color cor, String texto) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        item.setBackground(COR_PAINEL);
        JPanel quadrado = new JPanel();
        quadrado.setBackground(cor);
        quadrado.setPreferredSize(new Dimension(12, 12));
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Monospaced", Font.PLAIN, 10));
        label.setForeground(COR_TEXTO);
        item.add(quadrado);
        item.add(label);
        return item;
    }

    private JLabel criarLabel(String texto, int tamanho, Color cor) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Monospaced", Font.PLAIN, tamanho));
        label.setForeground(cor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // --- Atualização do painel lateral ---

    private void atualizarPainel() {
        if (indiceAtual >= filaNavios.length) return;

        TipoNavio atual = filaNavios[indiceAtual];
        labelNavio.setText(atual.getNome() + " (" + atual.getTamanho() + ")");
        labelDirecao.setText(nomeDirecao(direcaoAtual));

        painelFila.removeAll();
        for (int i = indiceAtual; i < filaNavios.length; i++) {
            TipoNavio tipo = filaNavios[i];
            String prefixo = (i == indiceAtual) ? "▶ " : "  ";
            JLabel item = criarLabel(prefixo + tipo.getNome(), 12,
                i == indiceAtual ? COR_DESTAQUE : new Color(100, 140, 180));
            painelFila.add(item);
            painelFila.add(Box.createVerticalStrut(2));
        }
        painelFila.revalidate();
        painelFila.repaint();
    }

    private String nomeDirecao(Direcao d) {
        return switch (d) {
            case NORTE -> "↑ NORTE";
            case SUL   -> "↓ SUL";
            case LESTE -> "→ LESTE";
            case OESTE -> "← OESTE";
        };
    }

    // --- Rotação ---

    private void rotacionar() {
        direcaoAtual = switch (direcaoAtual) {
            case LESTE -> Direcao.SUL;
            case SUL   -> Direcao.OESTE;
            case OESTE -> Direcao.NORTE;
            case NORTE -> Direcao.LESTE;
        };
        labelDirecao.setText(nomeDirecao(direcaoAtual));
        painelTabuleiro.repaint();
    }

    // --- Posicionamento ---

    private void tentarPosicionar(int linha, int coluna) {
        if (indiceAtual >= filaNavios.length) return;

        try {
            jogador.posicionarNavio(filaNavios[indiceAtual], linha, coluna, direcaoAtual);
            indiceAtual++;

            if (indiceAtual >= filaNavios.length) {
                painelTabuleiro.repaint();
                mostrarMensagemFim();
            } else {
                atualizarPainel();
                painelTabuleiro.repaint();
            }
        } catch (PosicionamentoInvalidoException e) {
            // Preview já mostra vermelho — não precisa de popup
        }
    }

    private void mostrarMensagemFim() {
        int opcao = JOptionPane.showConfirmDialog(
            this,
            "Todos os navios posicionados!\nPronto para batalhar?",
            "Posicionamento concluído",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        if (opcao == JOptionPane.OK_OPTION) {
            dispose();
            aoTerminar.run();
        }
    }

    // --- Painel do tabuleiro ---

    private class TabuleiroPanel extends JPanel {

        public TabuleiroPanel() {
            int tamanho = jogador.getTabuleiro().getTamanho();
            int lado = TAMANHO_HEADER + tamanho * TAMANHO_CELULA + 20;
            setPreferredSize(new Dimension(lado, lado));
            setBackground(COR_FUNDO);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int[] cel = pixelParaCelula(e.getX(), e.getY());
                    hoverLinha  = cel[0];
                    hoverColuna = cel[1];
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int[] cel = pixelParaCelula(e.getX(), e.getY());
                    if (cel[0] >= 0) tentarPosicionar(cel[0], cel[1]);
                    requestFocusInWindow();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoverLinha  = -1;
                    hoverColuna = -1;
                    repaint();
                }
            });

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_R) rotacionar();
                }
            });
        }

        private int[] pixelParaCelula(int px, int py) {
            int col = (px - TAMANHO_HEADER) / TAMANHO_CELULA;
            int lin = (py - TAMANHO_HEADER) / TAMANHO_CELULA;
            int tamanho = jogador.getTabuleiro().getTamanho();
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

            Tabuleiro tab = jogador.getTabuleiro();
            int tamanho = tab.getTamanho();

            // Calcula preview via Tabuleiro (lógica de negócio no Model)
            List<int[]> preview = new ArrayList<>();
            boolean previewValido = false;
            if (hoverLinha >= 0 && indiceAtual < filaNavios.length) {
                TipoNavio tipo = filaNavios[indiceAtual];
                preview = tab.calcularPosicoes(hoverLinha, hoverColuna, direcaoAtual, tipo.getTamanho());
                previewValido = tab.posicionamentoValido(hoverLinha, hoverColuna, direcaoAtual, tipo.getTamanho());
            }

            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {
                    int x = TAMANHO_HEADER + j * TAMANHO_CELULA;
                    int y = TAMANHO_HEADER + i * TAMANHO_CELULA;

                    Color cor = tab.getCelula(i, j).temNavio() ? COR_NAVIO : COR_CELULA;

                    for (int[] pos : preview) {
                        if (pos[0] == i && pos[1] == j) {
                            cor = previewValido ? COR_PREVIEW_OK : COR_PREVIEW_ERRO;
                            break;
                        }
                    }

                    g2.setColor(cor);
                    g2.fillRoundRect(x + 1, y + 1, TAMANHO_CELULA - 2, TAMANHO_CELULA - 2, 6, 6);
                    g2.setColor(COR_CELULA_BORDA);
                    g2.drawRoundRect(x + 1, y + 1, TAMANHO_CELULA - 2, TAMANHO_CELULA - 2, 6, 6);
                }
            }

            // Headers --- números
            g2.setFont(new Font("Monospaced", Font.BOLD, 12));
            g2.setColor(COR_DESTAQUE);
            for (int j = 0; j < tamanho; j++) {
                int x = TAMANHO_HEADER + j * TAMANHO_CELULA + TAMANHO_CELULA / 2;
                drawCentered(g2, String.valueOf(j + 1), x, TAMANHO_HEADER / 2 + 5);
            }

            // Headers --- letras
            for (int i = 0; i < tamanho; i++) {
                int y = TAMANHO_HEADER + i * TAMANHO_CELULA + TAMANHO_CELULA / 2;
                drawCentered(g2, String.valueOf((char) ('A' + i)), TAMANHO_HEADER / 2, y + 5);
            }
        }

        private void drawCentered(Graphics2D g2, String texto, int cx, int cy) {
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto, cx - fm.stringWidth(texto) / 2, cy);
        }
    }
}