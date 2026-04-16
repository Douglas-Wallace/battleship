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
                    
                System.out.println("Digite a cordenada: ");
                String entrada = sc.next();

                char colunaChar = entrada.toUpperCase().charAt(0);
                int coluna = colunaChar - 'A';
                int linha = Integer.parseInt(entrada.substring(1));
                linha--;
                System.out.println("coluna: " + coluna);
                System.out.println("linha: " + linha);

        
                resultado = inimigo.tabuleiro.atacar(linha, coluna);
        
                System.out.println("Resultado: " + resultado);
            } catch (PosicionamentoInvalidoException e) {
                System.out.println("Posição inválida. Tente novamente.");
                resultado = null;
            } catch (Exception e) {
                    System.out.println("Entrada inválida. Tente novamente.");
                    sc.nextLine();
                    resultado = null;
                }
            } while(resultado == StatusCelula.JA_ATACADO || resultado == null);
    }
    
}