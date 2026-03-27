package com.batalhanaval.model.entities;

public class JogadorTerminal extends Jogador{

    public JogadorTerminal(String nome) {
        super(nome);
    }
    
    
    
    @Override
    public void jogar(){
  
    }
    
    @Override   
    public void exibirTabuleiro() {
        tabuleiro.exibir();
    }

}