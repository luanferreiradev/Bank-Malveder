// Solicitacao.java
package model;

import model.enums.Status_Solicitacao;

public class Solicitacao {
    private int id;
    private String documento;
    private String tipoConta;
    private Status_Solicitacao status;
    private String senha;
    private Long numeroConta;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String dataNascimento;
    private String numeroCasa;
    private String nome;
    private String telefone;

    public Solicitacao(int id, String documento, String senha, String tipoConta, String endereco, String numeroCasa, String bairro, String cidade, String estado, String dataNascimento, Status_Solicitacao status) {
        this.id = id;
        this.documento = documento;
        this.senha = senha;
        this.tipoConta = tipoConta;
        this.endereco = endereco;
        this.numeroCasa = numeroCasa;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.dataNascimento = dataNascimento;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Status_Solicitacao getStatus() {
        return status;
    }

    public void setStatus(Status_Solicitacao status) {
        this.status = status;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Long getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(Long numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Documento: " + documento + ", Tipo: " + tipoConta + ", Status: " + status + ", Número da Conta: " + numeroConta + ", Endereço: " + endereco + ", Bairro: " + bairro + ", Cidade: " + cidade + ", Estado: " + estado + ", Data de Nascimento: " + dataNascimento;
    }
}