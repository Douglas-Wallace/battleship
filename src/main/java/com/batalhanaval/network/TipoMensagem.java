package com.batalhanaval.network;

public enum TipoMensagem {
    // Conexão
    INICIO,         // avisa que terminou o posicionamento

    // Jogada
    ATAQUE,         // envia coordenada do ataque
    RESULTADO,      // responde com resultado do ataque

    // Fim
    DERROTA,        // avisa que perdeu (o outro sabe que venceu)

    // Conexão perdida
    DESCONECTADO,   // conexão encerrada

}