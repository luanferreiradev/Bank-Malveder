package model;

public class Endereco {
    private long id;
    private String cep;
    private String local;
    private String bairro;
    private String cidade;
    private String estado;
    private String numeroCasa;

    //gere um construtor vazio e outro com todos os atributos
    public Endereco() {
    }

    public Endereco(String cep, String local, String bairro, String cidade, String estado) {
        this.cep = cep;
        this.local = local;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public long getId() {
        return id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    //gere um toString para exibir todos os atributos
    @Override
    public String toString() {
        return "Endereco{" + "id=" + id + ", cep=" + cep + ", local=" + local + ", bairro=" + bairro + ", cidade=" + cidade + ", estado=" + estado + '}';
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public String getEnderecoCompleto() {
        return local + ", " + numeroCasa + ", " + bairro + ", " + cidade + ", " + estado + ", " + cep;
    }
}
