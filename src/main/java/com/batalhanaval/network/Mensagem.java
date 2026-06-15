package com.batalhanaval.network;

import com.batalhanaval.model.enums.StatusCelula;
import java.io.Serializable;

public class Mensagem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final TipoMensagem tipo;
    private Integer linha;
    private Integer coluna;
    private StatusCelula resultado;

    // Mensagens sem dados (CONECTADO, INICIO, SEU_TURNO, etc)
    public Mensagem(TipoMensagem tipo) {
        this.tipo = tipo;
    }

    // ATAQUE — carrega coordenada
    public Mensagem(TipoMensagem tipo, int linha, int coluna) {
        this.tipo   = tipo;
        this.linha  = linha;
        this.coluna = coluna;
    }

    // RESULTADO — carrega status + coordenada do ataque que originou o resultado
    public Mensagem(TipoMensagem tipo, StatusCelula resultado, int linha, int coluna) {
        this.tipo      = tipo;
        this.resultado = resultado;
        this.linha     = linha;
        this.coluna    = coluna;
    }

    public TipoMensagem getTipo()        { return tipo;      }
    public Integer getLinha()            { return linha;     }
    public Integer getColuna()           { return coluna;    }
    public StatusCelula getResultado()   { return resultado; }
}