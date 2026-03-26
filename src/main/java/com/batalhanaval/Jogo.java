package com.batalhanaval;

import com.batalhanaval.model.Navio;
import com.batalhanaval.model.Tabuleiro;

public class Jogo {
    public static void main(String[] args){
        Tabuleiro jogo = new Tabuleiro();
        jogo.iniciar();
        jogo.exibir();
        System.out.println("----------------------");
        jogo.adicionarNavio(new Navio(1 , 2));
        jogo.adicionarNavio(new Navio(1 , 2));
        jogo.adicionarNavio(new Navio(1 , 3));

        jogo.exibir();
        
  
    }
} 