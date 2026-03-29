package com.batalhanaval;

import com.batalhanaval.model.entities.Navio;
import com.batalhanaval.model.entities.PortaAvioes;
import com.batalhanaval.model.entities.Submarino;
import com.batalhanaval.model.entities.Tabuleiro;
import java.sql.SQLOutput;

public class Batalhanaval {
    public static void main(String[] args){
        Tabuleiro tabuleiro = new Tabuleiro();
        Navio submarino = new Submarino();
        Navio carrier = new PortaAvioes();
        
        try{
            System.out.println("-------------------------");
            tabuleiro.adicionarNavio(submarino, 9, 5, 'S');
            tabuleiro.exibir();
            System.out.println("-------------------------");
            tabuleiro.adicionarNavio(carrier, 3, 5, 'S');
            tabuleiro.exibir();
    
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
        
        
    }
} 