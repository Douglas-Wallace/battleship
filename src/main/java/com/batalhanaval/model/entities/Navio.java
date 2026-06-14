package com.batalhanaval.model.entities;

import com.batalhanaval.model.enums.TipoNavio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Navio {

    private List<Parte> partes;
    private TipoNavio tipo;

    public Navio(TipoNavio tipo) {
        setTipo(tipo);
        setPartes();
    }

    private void setPartes(){
       this.partes = new ArrayList<>();

       for (int i = 0; i < tipo.getTamanho(); i++) {
            partes.add(new Parte(this));
        }
    }

    public List<Parte> getPartes() {
        return Collections.unmodifiableList(partes);
    }

    private void setTipo(TipoNavio tipo){
        this.tipo = tipo;
    }

    public TipoNavio getTipo() {
        return tipo;
    }

    public boolean foiAfundado() {
        for (Parte parte : partes) {
            if (!parte.foiAtingida()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return tipo.getNome();
    }
}
