package com.batalhanaval.model.entities;

public class Parte {
    private Navio navio;
    private boolean atingida;

    public Parte(Navio navio){
        this.navio = navio;
        this.atingida = false;
    }
    
    public void atingir(){
        this.atingida = true;
    }
    
    public boolean foiAtingida(){
        return atingida;
    }
    
    public Navio getNavio(){
        return navio;
    }
}