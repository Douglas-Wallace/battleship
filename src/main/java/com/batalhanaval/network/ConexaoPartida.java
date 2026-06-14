package com.batalhanaval.network;

import java.io.IOException;

public interface ConexaoPartida {
    void conectar() throws IOException;
    void enviar(Mensagem mensagem) throws IOException;
    Mensagem receber() throws IOException;
    void encerrar();
}