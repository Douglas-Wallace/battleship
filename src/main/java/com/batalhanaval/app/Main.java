package com.batalhanaval.app;

import com.batalhanaval.controller.JogoController;
import com.batalhanaval.model.entities.Jogador;
import com.batalhanaval.model.entities.Jogo;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
		String nome = JOptionPane.showInputDialog(null, "Digite seu nome:", "Batalha Naval",
				JOptionPane.QUESTION_MESSAGE);

		if (nome == null || nome.isBlank())
			nome = "Jogador";

		Jogador jogador = new Jogador(nome.trim());
		Jogo jogo = new Jogo(jogador);

		JogoController controller = new JogoController(jogo);
		controller.iniciar();
	}
}

