package com.batalhanaval.model.entities;

import com.batalhanaval.model.entities.Tabuleiro;
import com.batalhanaval.model.entities.Jogador;

public class Jogo {
    
    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogadorAtual;
    private Jogador jogadorInimigo;
    
    public Jogo(Jogador jogador1, Jogador jogador2){
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        
        this.jogadorAtual = jogador1;
        this.jogadorInimigo = jogador2;
    }
    
    public void iniciar() {
        
        while(true) {
            
            System.out.println("\n----------------");
            System.out.println("Vez de: " + jogadorAtual.nome);
            
            System.out.println("\nSeu tabuleiro: ");
            jogadorAtual.exibirTabuleiroIndividual();
            
            System.out.println("\nTabuleiro inimigo: ");
            jogadorAtual.exibirTabuleiroInimigo(jogadorInimigo);
            
            //faz jogada
            jogadorAtual.jogar(jogadorInimigo);
            
            //vitória
            if(verificarVitoria()){
                System.out.println("\n Vencedor: " + jogadorAtual.nome);
                break;
            }
            
            trocarTurno();
        }
    }
    
    private void trocarTurno() {
        Jogador temp = jogadorAtual;
        jogadorAtual = jogadorInimigo;
        jogadorInimigo = temp;
    }
    
    private boolean verificarVitoria() {
        return jogadorInimigo.naviosAfundados();
    }
    
}
