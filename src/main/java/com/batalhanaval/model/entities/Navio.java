package com.batalhanaval.model.entities;

import com.batalhanaval.enums.TipoNavio;
import java.util.ArrayList;

public class Navio {
    private ArrayList<Parte> partes;
    private TipoNavio tipo;

    public Navio(TipoNavio tipo) {
        this.partes = new ArrayList<>();
        this.tipo = tipo;
        
        
        for(int i = 0; i < tipo.getTamanho(); i++){
            partes.add(new Parte(this));
        }
    }
    
    
    public ArrayList<Parte> getPartes(){
        return partes;
    }
    
    public TipoNavio getTipo(){
        return tipo;
    }
    
    public boolean foiAfundado() {
        for (Parte parte : partes){
            if(!parte.foiAtingida()){
               return false;
            }
        }
        return true;
    }
}