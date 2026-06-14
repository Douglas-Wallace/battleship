package com.batalhanaval.model.entities;

import com.batalhanaval.model.enums.StatusCelula;

public class Celula {

    private Parte parte; // null = água
    private boolean atacada;
    private StatusCelula statusAtaque;

    public Celula() {
    }

    // ── Métodos de negócio ────────────────────────────────────────────────────

    /** Ataca a célula real (tabuleiro próprio). Marca como atacada e atinge a parte se houver navio. */
    public void atacar() {
        this.atacada = true;
        if (parte != null) {
            parte.atingir();
        }
    }

    /** Registra o resultado de um ataque no tabuleiro de rastreamento. */
    public void registrarResultado(StatusCelula resultado) {
        this.atacada = true;
        this.statusAtaque = resultado;
    }

    public boolean temNavio() {
        return parte != null;
    }

    public boolean foiAtacada() {
        return atacada;
    }

    public boolean navioAfundado() {
        if (parte == null) return false;
        return parte.getNavio().foiAfundado();
    }

    // ── Getters e Setters ─────────────────────────────────────────────────────

    public void setParte(Parte parte) {
        this.parte = parte;
    }

    public Parte getParte() {
        return this.parte;
    }

    public StatusCelula getStatusAtaque() {
        return statusAtaque;
    }
}