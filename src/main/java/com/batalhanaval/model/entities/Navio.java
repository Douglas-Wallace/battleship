package com.batalhanaval.model.entities;

import com.batalhanaval.model.exceptions.OrientacaoInvalidaException;

public abstract class Navio {
    protected int x, y;
    protected int tamanho;
    protected char orientacao;
    protected Parte[] partes;
    protected boolean afundado;

    public Navio(int x, int y, int tamanho, char orientacao) {
        this.x = x;
        this.y = y;
        this.tamanho = tamanho;
        this.orientacao = orientacao;
        
        this.afundado = false;
        this.partes = new Parte[tamanho];
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }
    

    public int getTamanho() {
        return tamanho;
    }

    public char getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(char orientacao) {
        if(orientacao != 'S' || orientacao != 'N' || orientacao != 'L' || orientacao != 'O'){
           throw new OrientacaoInvalidaException("[EXCEPTION] Navio - orientaçâo invalida!");
        }
        
        this.orientacao = orientacao;
    }
    
    public void checarPartes(){
        int i = 0;
        for (Parte parte : partes){
            if(parte.getAfundado()){
                i++;
            }
        }
        if(i == tamanho){
            afundado = true;
        }
    } 
}