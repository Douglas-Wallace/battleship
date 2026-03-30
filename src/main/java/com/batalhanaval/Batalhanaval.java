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
            tabuleiro.exibirProprio();
            System.out.println("-------------------------");
            tabuleiro.adicionarNavio(carrier, 4, 5, 'L');
            tabuleiro.exibirProprio();
            
            
            System.out.println("-------------------------");
            System.out.println(tabuleiro.atacar(9, 5));
            tabuleiro.exibirProprio();
     
            System.out.println("-------------------------");
            System.out.println(tabuleiro.atacar(10, 5));
            tabuleiro.exibirProprio();
    
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
        
        
    }
} 