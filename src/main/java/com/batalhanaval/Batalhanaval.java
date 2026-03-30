package com.batalhanaval;

import com.batalhanaval.model.entities.Navio;
import com.batalhanaval.model.entities.PortaAvioes;
import com.batalhanaval.model.entities.Submarino;
import com.batalhanaval.model.entities.Tabuleiro;
import java.sql.SQLOutput;

public class Batalhanaval {
    // MAIN PRA TESTES
    public static void main(String[] args){
        Tabuleiro tabuleiro = new Tabuleiro();
        Navio submarino = new Submarino();
        Navio carrier = new PortaAvioes();
        
        try{
            System.out.println("-------------------------");
            tabuleiro.adicionarNavio(submarino, 9, 5, 'S');
            tabuleiro.exibir();
            System.out.println("-------------------------");
            tabuleiro.adicionarNavio(carrier, 4, 5, 'L');
            tabuleiro.exibir();
            
            
            System.out.println("-------------------------");
            System.out.println(tabuleiro.atacar(9, 5));
            tabuleiro.exibir();
     
            System.out.println("-------------------------");
            System.out.println(tabuleiro.atacar(10, 5));
            tabuleiro.exibir();
    
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
        
        
    }
} 