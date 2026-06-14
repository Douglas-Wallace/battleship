package com.batalhanaval.network;

import java.io.*;
import java.net.*;

public class ClientePartida implements ConexaoPartida {

    private static final int PORTA = 5000;

    private final String enderecoServidor;
    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;

    public ClientePartida(String enderecoServidor) {
        this.enderecoServidor = enderecoServidor;
    }

    @Override
    public void conectar() throws IOException {
        socket = new Socket(enderecoServidor, PORTA);
        System.out.println("Conectado ao servidor: " + enderecoServidor);

        saida  = new ObjectOutputStream(socket.getOutputStream());
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
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao encerrar cliente: " + e.getMessage());
        }
    }
}