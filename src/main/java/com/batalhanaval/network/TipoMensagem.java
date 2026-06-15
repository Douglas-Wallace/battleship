package com.batalhanaval.network;

public enum TipoMensagem {
    // Conexão
    CONECTADO,      // cliente → servidor: "cheguei"
    INICIO,         // servidor → cliente: "pode posicionar"

    // Turno
    SEU_TURNO,      // servidor → cliente: "é sua vez de atacar"
    TURNO_INIMIGO,  // servidor → cliente: "aguarde, inimigo atacando"

    // Jogada
    ATAQUE,         // cliente → servidor: "ataquei linha X coluna Y"
    RESULTADO,      // servidor → cliente: "resultado do ataque foi Z"

    // Fim
    VITORIA,        // servidor → cliente: "você venceu"
    DERROTA,        // servidor → cliente: "você perdeu"

    // Conexão perdida
    DESCONECTADO    // qualquer lado: "conexão encerrada"
}