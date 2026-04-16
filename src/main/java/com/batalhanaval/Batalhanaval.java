package com.batalhanaval;

import com.batalhanaval.enums.Direcao;
import com.batalhanaval.model.entities.Navio;
import com.batalhanaval.model.entities.PortaAvioes;
import com.batalhanaval.model.entities.Submarino;
import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.JogadorTerminal;
import com.batalhanaval.model.entities.Jogo;

public class Batalhanaval {
    // MAIN PRA TESTES
    public static void main(String[] args){

        try{
            Jogador jogador1 = new JogadorTerminal("Douglas");
            Jogador jogador2 = new JogadorTerminal("Isabella");
        
            jogador1.posicionarNavio(new PortaAvioes(), 1, 1, Direcao.LESTE);
            jogador2.posicionarNavio(new Submarino(), 3, 3, Direcao.SUL);
        
            Jogo jogo = new Jogo(jogador1, jogador2);
            jogo.iniciar();
    
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
        
        
    }
} 