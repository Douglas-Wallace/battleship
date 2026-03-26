package com.batalhanaval.model;

public class Navio {
    private int x, y;
    private int tamanho;
    private char orientacao;

    public Navio(int posicaoX,  int posicaoY) {
        this.x = posicaoX;
        this.y = posicaoY;
    }

    public int getX() {
        return x - 1;
    }
    public int getY() {
        return y - 1;
    }

    
 
    
    
}