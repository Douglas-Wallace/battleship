package com.batalhanaval.app;

import com.batalhanaval.controller.JogoController;
import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.Jogo;

public class Main {

    public static void main(String[] args) {
        Jogador jogador1 = new Jogador("Douglas");
        Jogador jogador2 = new Jogador("Isabella");

        Jogo jogo = new Jogo(jogador1, jogador2);
        JogoController controller = new JogoController(jogo);

        controller.iniciar();
    }
}

