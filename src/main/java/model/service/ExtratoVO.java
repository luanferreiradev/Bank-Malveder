package model.service;

// Classe que representa um extrato
public class ExtratoVO {
    private String nome;
    private String documento;
    private String numeroConta;
    private TransacaoVO transacao;

    public ExtratoVO(String nome, String documento, String numeroConta, TransacaoVO transacao) {
        this.nome = nome;
        this.documento = documento;
        this.numeroConta = numeroConta;
        this.transacao = transacao;
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public TransacaoVO getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoVO transacao) {
        this.transacao = transacao;
    }
}