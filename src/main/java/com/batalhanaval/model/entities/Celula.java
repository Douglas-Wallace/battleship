package com.batalhanaval.model.entities;

import com.batalhanaval.model.enums.StatusCelula;

public class Celula {
    private Parte parte; //null = aguá
    private boolean atacada;
    private StatusCelula statusAtaque;
    
    public Celula(){
    }
    
    public void registrarResultado(StatusCelula resultado) {
        this.statusAtaque = resultado;
    }
    
    public StatusCelula getStatusAtaque() {
        return statusAtaque;
    }
    
    public boolean temNavio(){
        return parte != null;              
    }
    
    public boolean foiAtacada(){
        return atacada;
    }
    
    public void atacar(){
        this.atacada = true;
        
        if(parte != null){
            parte.atingir();
        }
    }
    
    public void setParte(Parte parte){
        this.parte = parte;
    }
    
    public Parte getParte(){
        return this.parte;
    }
    
    public boolean navioAfundado(){
        if(parte == null){
            return false;
        }
        return parte.getNavio().foiAfundado();
    }

}