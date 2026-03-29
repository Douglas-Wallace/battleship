package com.batalhanaval.model.entities;

public class Tabuleiro {
    private final int tamanho = 10;
    private Celula[][] tabuleiro;

    public Tabuleiro(){
        tabuleiro = new Celula[tamanho][tamanho];
    }
    
    public void adicionarNavio(Navio navio, int x, int y, char direcaoo){
        validarDirecao(direcaoo);
        
        
    }
    
    public void validarDirecao(char direcao){
        if(direcao != 'N' && direcao != 'S' && direcao != 'O' && direcao != 'L'){
            throw new RuntimeException("Tabuleiro - Direção inválida");
        }
    }
   
}



