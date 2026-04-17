package com.batalhanaval;

import com.batalhanaval.enums.Direcao;
import com.batalhanaval.enums.TipoNavio;
import com.batalhanaval.model.entities.Navio;
import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.JogadorTerminal;
import com.batalhanaval.model.entities.Jogo;

public class Batalhanaval {
    
    public static void main(String[] args){

        try{
            Jogador jogador1 = new JogadorTerminal("Douglas");
            Jogador jogador2 = new JogadorTerminal("Isabella");
        
            jogador1.posicionarNavio(new Navio(TipoNavio.PORTA_AVIAO), 1, 1, Direcao.LESTE);
            jogador2.posicionarNavio(new Navio(TipoNavio.SUBMARINO), 3, 3, Direcao.SUL);
        
            Jogo jogo = new Jogo(jogador1, jogador2);
            jogo.iniciar();
    
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
        
             
    }
} 