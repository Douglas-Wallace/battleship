package com.batalhanaval.model.entities;

public class Parte {
    private int x, y;
    private boolean afundado;

    public Parte(int x, int y) {
        this.x = x;
        this.y = y;
        this.afundado = false;
    }
    
    
    public void afundar(){
        afundado = true;
    }
    
    public boolean getAfundado(){
        return afundado;
    }
}