package com.batalhanaval.network;

import java.io.*;
import java.net.*;

public class ServidorPartida implements ConexaoPartida {

    private static final int PORTA = 5000;

    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;

    @Override
    public void conectar() throws IOException {
        serverSocket = new ServerSocket(PORTA);
        System.out.println("Aguardando conexão na porta " + PORTA + "...");
        socket = serverSocket.accept();
        System.out.println("Cliente conectado: " + socket.getInetAddress());

        // IMPORTANTE: saida antes de entrada para evitar deadlock
        saida  = new ObjectOutputStream(socket.getOutputStream());
        saida.flush();
        entrada = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void enviar(Mensagem mensagem) throws IOException {
        saida.writeObject(mensagem);
        saida.flush();
    }

    @Override
    public Mensagem receber() throws IOException {
        try {
            return (Mensagem) entrada.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Mensagem desconhecida recebida", e);
        }
    }

    @Override
    public void encerrar() {
        try {
            if (socket       != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erro ao encerrar servidor: " + e.getMessage());
        }
    }
}