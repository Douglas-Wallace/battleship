package com.batalhanaval.model.entities;

import java.util.Scanner;

public class JogadorTerminal extends Jogador{

    public JogadorTerminal(String nome) {
        super(nome);
    }
    
    
    
    @Override
    public void jogar(Jogador inimigo){
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Digite a linha: ");
        int linha = sc.nextInt();
        
        System.out.println("Digite a coluna: ");
        int coluna = sc.nextInt();
        
        String resultado = inimigo.tabuleiro.atacar(linha, coluna);
        
        System.out.println("Resultado: " + resultado);
    }
    
    @Override   
    public void exibirTabuleiro() {
        tabuleiro.getVisaoPropria();
    }
}