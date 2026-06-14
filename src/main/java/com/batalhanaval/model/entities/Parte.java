package com.batalhanaval.model.entities;

public class Parte {
    private Navio navio;
    private boolean atingida = false;

    public Parte(Navio navio){
        setNavio(navio);
    }
    
    public void atingir(){
        this.atingida = true;
    }
    
    public boolean foiAtingida(){
        return atingida;
    }

    private void setNavio(Navio navio){
        this.navio = navio;
    }
    
    public Navio getNavio(){
        return navio;
    }
   
}