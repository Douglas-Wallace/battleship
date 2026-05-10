package com.batalhanaval.model.util;

public class CoordenadaParser {

    public static int[] parse(String entrada) {
        entrada = entrada.toUpperCase();

        char colunaChar = entrada.charAt(0);
        int coluna = colunaChar - 'A';

        int linha = Integer.parseInt(entrada.substring(1)) - 1;

        return new int[]{coluna, linha};
    }
}
