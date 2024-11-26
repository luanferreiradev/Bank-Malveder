package model.impl;

import model.Cliente;

import java.time.LocalDate;


// Classe ClientePJ que herda de Cliente
public class ClientePJ extends Cliente {
    private String cnpj;

    public ClientePJ(int idUsuario, String nome, String documento, LocalDate dataNascimento, String telefone, String tipoUsuario, String senha, int idCliente, String cnpj) {
        super(idUsuario, nome, documento, dataNascimento, telefone, tipoUsuario, senha, idCliente);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}