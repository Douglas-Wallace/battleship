package com.batalhanaval.model.entities;

public abstract class Jogador {

    protected Tabuleiro tabuleiro;
    protected String nome;

    public Jogador(String nome) {
        this.nome = nome;

        this.tabuleiro = new Tabuleiro();
    }

    public abstract void jogar(Jogador inimigo);

    public abstract void exibirTabuleiro();

    public void posicionarNavio(Navio navio, int linha, int coluna, char direcao) {
        tabuleiro.adicionarNavio(navio, linha, coluna, direcao);

    }

    public void exibirTabuleiroIndividual() {
        char[][] tabuleiroProprio = tabuleiro.getVisaoPropria();
        
        for(int i = 0; i < tabuleiro.getTamanho(); i++){
            for(int j = 0; j < tabuleiro.getTamanho(); j++){
                switch (tabuleiroProprio[i][j]) {
                    case '~':
                        System.out.print("~ ");
                        break;
                    case 'X':
                        System.out.print("X ");
                        break;
                    case '-':
                        System.out.print("- ");
                        break;
                    case 'O':
                        System.out.print("O ");
                        break;
                    case 'N':
                        System.out.print("N ");
                        break;    
                    default:
                        throw new AssertionError();
                }
            }
            System.out.println("");
        }
    }
    
    public void exibirTabuleiroInimigo(Jogador inimigo){
        char[][] tabuleiroInimigo = inimigo.tabuleiro.getVisaoInimigo();
        
        for(int i = 0; i < tabuleiro.getTamanho(); i++){
            for(int j = 0; j < tabuleiro.getTamanho(); j++){
                switch (tabuleiroInimigo[i][j]) {
                    case '~':
                        System.out.print("~ ");
                        break;
                    case 'X':
                        System.out.print("X ");
                        break;
                    case '-':
                        System.out.print("- ");
                        break;
                    case 'O':
                        System.out.print("O ");
                        break;
                    default:
                        throw new AssertionError();
                }
            }
            System.out.println("");
        }
    }
       
    public boolean naviosAfundados() {
        return tabuleiro.naviosAfundados();
    }

}
