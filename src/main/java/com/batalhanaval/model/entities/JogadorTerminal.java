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
    int tamanho = tabuleiro.getTamanho();

    for (int i = 0; i <= tamanho; i++) {
        for (int j = 0; j <= tamanho; j++) {

            if (i == 0 && j == 0) {
                System.out.print("   ");

            } else if (i == 0) {
                System.out.print((char) ('A' + j - 1) + " ");

            } else if (j == 0) {
                System.out.printf("%2d ", i);

            } else {
                System.out.print(visao[i - 1][j - 1] + " ");
            }
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
    public void atacar(Jogador inimigo){
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