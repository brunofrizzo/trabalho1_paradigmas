public class Usuario {

    private String nome;
    private String login;
    private Boolean multa = false;
    private Integer prof;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getMulta() {
        return multa;
    }

    public void setMulta(Boolean multa) {
        this.multa = multa;
    }

    public Integer getProf() {
        return prof;
    }

    public void setProf(Integer prof) {
        this.prof = prof;
    }

}
