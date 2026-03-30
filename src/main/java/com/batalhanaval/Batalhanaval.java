package com.batalhanaval;

import com.batalhanaval.model.entities.Navio;
import com.batalhanaval.model.entities.PortaAvioes;
import com.batalhanaval.model.entities.Submarino;
import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.JogadorTerminal;
import java.sql.SQLOutput;

public class Batalhanaval {
    // MAIN PRA TESTES
    public static void main(String[] args){
        Tabuleiro tabuleiro = new Tabuleiro();
        Navio submarino = new Submarino();
        Navio carrier = new PortaAvioes();
        
        //Testando visualização navio inimigo
        Jogador jogador1 = new JogadorTerminal("Jogador 1");
        Jogador jogador2 = new JogadorTerminal("Jogador 2");
        
        jogador1.posicionarNavio(new PortaAvioes(), 1, 1, 'L');
        jogador2.posicionarNavio(new Submarino(), 3, 3, 'S');
        
        System.out.println("TABULEIRO JOGADOR 1");
        jogador1.exibirTabuleiroIndividual();
        
        System.out.println("TABULEIRO JOGADOR 2");
        jogador2.exibirTabuleiroInimigo(jogador2);
        
        
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