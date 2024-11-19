package model.impl;

import model.Cliente;
import model.Conta;

public class ClientePF extends Cliente {
    private String cpf;

    public ClientePF(String documento, String senha, boolean isPessoaFisica, String endereco, String numeroCasa, String bairro, String cidade, String estado, String dataNascimento, String cpf, String nome, String telefone) {
        super(documento, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, dataNascimento, nome, telefone);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }
}
