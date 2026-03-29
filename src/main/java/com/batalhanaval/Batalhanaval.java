package com.batalhanaval;

import com.batalhanaval.model.entities.Navio;
import com.batalhanaval.model.entities.Submarino;
import com.batalhanaval.model.entities.Tabuleiro;
import java.sql.SQLOutput;

public class Batalhanaval {
    public static void main(String[] args){
        Tabuleiro tabuleiro = new Tabuleiro();
        Navio submarino = new Submarino();
        
        try{
            System.out.println("-------------------------");
            tabuleiro.adicionarNavio(submarino, 9, 5, 'S');
            tabuleiro.exibir();
    
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
        
        
    }
} 