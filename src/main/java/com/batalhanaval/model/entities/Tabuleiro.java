package com.batalhanaval.model.entities;

import com.batalhanaval.util.Validador;

public class Tabuleiro {
    private final int tamanho = 10;
    private char[][] tabuleiro;

    public Tabuleiro() {
        tabuleiro = new char[tamanho][tamanho];
    }
    
    
    public void adicionarNavio(Navio navio){
        if(tabuleiro[navio.getX()][navio.getY()] == '~') {
            tabuleiro[navio.getX()][navio.getY()] = 'N';
        } else {
            System.out.println("FALHA: adicionarNavio(), posicao ocupada");
        }
    }
    
    public void exibir(){
        //CABEÇALHO
        System.out.print("   ");
        for(int i = 0; i < tamanho; i++){
            System.out.print(" " + (char) ('A' + i) ); 
        }
        System.out.println();
        
        //CONTEUDO
        for(int linha = 0; linha < tamanho; linha++){
            
            System.out.printf("%2d ", linha + 1); 
            
            for(int coluna = 0; coluna < tamanho; coluna++){
                  System.out.print(" " + tabuleiro[linha][coluna]);  
            }
            System.out.println();
        }
    }
    
    public void iniciar(){ 
        for(int linha = 0; linha < tamanho; linha++){
            for(int coluna = 0; coluna < tamanho; coluna++){
                tabuleiro[linha][coluna] = '~';
            }
        }
    }
}



