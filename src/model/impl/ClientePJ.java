package model.impl;

import model.Cliente;
import model.Conta;

public class ClientePJ extends Cliente {
    private String cnpj;

    public ClientePJ(String documento, String senha, boolean isPessoaFisica, String endereco, String numeroCasa, String bairro, String cidade, String estado, String dataNascimento, String cnpj, String nome, String telefone) {
        super(documento, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, dataNascimento, nome, telefone);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }
}