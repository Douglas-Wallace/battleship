package com.batalhanaval.model.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoricoPartida {

    private static final String ARQUIVO = "historico.txt";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ---- Escrita ----

    public static void salvar(String nomeVencedor, int jogadas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            writer.write("=== Partida " + LocalDateTime.now().format(FORMATO) + " ===");
            writer.newLine();
            writer.write("Vencedor: " + nomeVencedor);
            writer.newLine();
            writer.write("Jogadas: " + jogadas);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    // ---- Leitura ----

    private static List<String> lerLinhas() {
        List<String> linhas = new ArrayList<>();

        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return linhas;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler histórico: " + e.getMessage());
        }

        return linhas;
    }

    public static String exibir() {
        List<String> linhas = lerLinhas();
        if (linhas.isEmpty()) return "Nenhuma partida registrada ainda.";

        StringBuilder sb = new StringBuilder();
        for (String linha : linhas) {
            sb.append(linha).append("\n");
        }
        return sb.toString();
    }
}