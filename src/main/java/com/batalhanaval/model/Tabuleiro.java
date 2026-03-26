package com.batalhanaval.model;

import com.batalhanaval.util.Validador;

public class Tabuleiro {
    private char[][] tabuleiro;

    public Tabuleiro() {
        tabuleiro = new char[10][10];
    }
    
    
    public void adicionarNavio(Navio navio){
        if(tabuleiro[navio.getX()][navio.getY()] == '~') {
            tabuleiro[navio.getX()][navio.getY()] = 'N';
        } else {
            System.out.println("FALHA: adicionarNavio(), posicao ocupada");
        }
    }
    
    public void exibir(){
        for(int linha = 0; linha < tabuleiro.length; linha++){
            for(int coluna = 0; coluna < tabuleiro[linha].length; coluna++){
                System.out.print(" " + tabuleiro[linha][coluna]);;
            }
            System.out.println();
        }
    }
    
    public void iniciar(){ 
        for(int linha = 0; linha < tabuleiro.length; linha++){
            for(int coluna = 0; coluna < tabuleiro[linha].length; coluna++){
                tabuleiro[linha][coluna] = '~';
            }
        }
    }
}



