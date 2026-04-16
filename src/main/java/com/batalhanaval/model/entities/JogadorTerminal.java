package com.batalhanaval.model.entities;

import com.batalhanaval.enums.StatusCelula;
import com.batalhanaval.model.exceptions.PosicionamentoInvalidoException;
import java.util.Scanner;

public class JogadorTerminal extends Jogador{
    private final Scanner sc = new Scanner(System.in);

    public JogadorTerminal(String nome) {
        super(nome);
    }
    
    private void imprimirVisao(char[][] visao) {
    for (int i = 0; i < tabuleiro.getTamanho(); i++) {
        for (int j = 0; j < tabuleiro.getTamanho(); j++) {
            System.out.print(visao[i][j] + " ");
        }
        System.out.println();
    }
}
    public void exibirTabuleiroIndividual() {
        imprimirVisao(tabuleiro.getVisaoPropria());
    }
    
    public void exibirTabuleiroInimigo(Jogador inimigo){
        imprimirVisao(inimigo.tabuleiro.getVisaoInimigo());
    }
    
    @Override
    public void jogar(Jogador inimigo){
        StatusCelula resultado;
        
            do {
                try{
                    System.out.println("Digite a linha: ");
                    int linha = sc.nextInt();
                    linha--;
                    
                    System.out.println("Digite a coluna: ");
                    int coluna = sc.nextInt();
                    coluna--;
        
                    resultado = inimigo.tabuleiro.atacar(linha, coluna);
        
                    System.out.println("Resultado: " + resultado);
                } catch (PosicionamentoInvalidoException e) {
                     e.printStackTrace();
                     resultado = null;
                }
            } while(resultado == StatusCelula.JA_ATACADO || resultado == null);
            
        
        
    }
    
}