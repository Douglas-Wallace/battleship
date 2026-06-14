
import com.batalhanaval.model.enums.StatusCelula;
import com.batalhanaval.network.TipoMensagem;
import java.io.Serializable;

public class Mensagem implements Serializable {

    private final TipoMensagem tipo;
    private Integer linha;        // usado em ATAQUE
    private Integer coluna;       // usado em ATAQUE
    private StatusCelula resultado; // usado em RESULTADO

    // Construtor para mensagens sem dados (CONECTADO, INICIO, SEU_TURNO, etc)
    public Mensagem(TipoMensagem tipo) {
        this.tipo = tipo;
    }

    // Construtor para ATAQUE
    public Mensagem(TipoMensagem tipo, int linha, int coluna) {
        this.tipo   = tipo;
        this.linha  = linha;
        this.coluna = coluna;
    }

    // Construtor para RESULTADO
    public Mensagem(TipoMensagem tipo, StatusCelula resultado) {
        this.tipo      = tipo;
        this.resultado = resultado;
    }

    // getters

    public TipoMensagem getTipo() {
        return tipo;
    }

    public Integer getLinha() {
        return linha;
    }

    public Integer getColuna() {
        return coluna;
    }

    public StatusCelula getResultado() {
        return resultado;
    }
    
}