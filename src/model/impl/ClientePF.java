package model.impl;

import model.Cliente;

import java.time.LocalDate;

// Classe ClientePF que herda de Cliente
public class ClientePF extends Cliente {
    private String cpf;

    public ClientePF(int idUsuario, String nome, String documento, LocalDate dataNascimento, String telefone, String tipoUsuario, String senha, int idCliente, String cpf) {
        super(idUsuario, nome, documento, dataNascimento, telefone, tipoUsuario, senha, idCliente);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}