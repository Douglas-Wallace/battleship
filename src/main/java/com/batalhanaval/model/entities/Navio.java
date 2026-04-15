package com.batalhanaval.model.entities;

import java.util.ArrayList;

public abstract class Navio {
    private ArrayList<Parte> partes;
    private int tamanho;

    public Navio(int tamanho) {
        this.tamanho = tamanho;
        this.partes = new ArrayList<>();
        
        
        for(int i = 0; i < tamanho; i++){
            partes.add(new Parte(this));
        }
    }
    
    public int getTamanho(){
        return tamanho;
    }
    
    
    public ArrayList<Parte> getPartes(){
        return partes;
    }
    //comentario para subir o pull
    public boolean foiAfundado() {
        for (Parte parte : partes){
            if(!parte.foiAtingida()){
               return false;
            }
        }
        return true;
    }
}