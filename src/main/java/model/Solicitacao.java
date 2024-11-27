package model;

import model.enums.StatusSolicitacao;

import java.time.LocalDate;


// Classe Solicitacao
public class Solicitacao {
    private int idSolicitacao;
    private String documento;
    private String senha;
    private boolean isPessoaFisica;
    private String endereco;
    private String numeroCasa;
    private String bairro;
    private String cidade;
    private String estado;
    private LocalDate dataNascimento;
    private String nome;
    private String telefone;
    private String tipoConta;
    private StatusSolicitacao status;

    public Solicitacao(int idSolicitacao, String documento, String senha, boolean isPessoaFisica, String endereco, String numeroCasa, String bairro, String cidade, String estado, LocalDate dataNascimento, String nome, String telefone, String tipoConta, StatusSolicitacao status) {
        this.idSolicitacao = idSolicitacao;
        this.documento = documento;
        this.senha = senha;
        this.isPessoaFisica = isPessoaFisica;
        this.endereco = endereco;
        this.numeroCasa = numeroCasa;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.dataNascimento = dataNascimento;
        this.nome = nome;
        this.telefone = telefone;
        this.tipoConta = tipoConta;
        this.status = status;
    }

    // Getters and Setters

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isPessoaFisica() {
        return isPessoaFisica;
    }

    public void setPessoaFisica(boolean pessoaFisica) {
        isPessoaFisica = pessoaFisica;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }
}