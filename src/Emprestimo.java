import org.joda.time.DateTime;

public class Emprestimo {

    private Usuario usuario;
    private Exemplar exemplar;
    private DateTime data_retirada;
    private DateTime data_devolucao;
    private DateTime data_limite_devolucao;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public DateTime getData_retirada() {
        return data_retirada;
    }

    public void setData_retirada(DateTime data_retirada) {
        this.data_retirada = data_retirada;
    }

    public DateTime getData_devolucao() {
        return data_devolucao;
    }

    public void setData_devolucao(DateTime data_devolucao) {
        this.data_devolucao = data_devolucao;
    }

    public DateTime getData_limite_devolucao() {
        return data_limite_devolucao;
    }

    public void setData_limite_devolucao(DateTime data_limite_devolucao) {
        this.data_limite_devolucao = data_limite_devolucao;
    }

}
