package model;

import java.time.LocalDate;

public class Cliente extends Usuario {
    private int idCliente;

    public Cliente(int idUsuario, String nome, String documento, LocalDate dataNascimento, String telefone, String tipoUsuario, String senha, int idCliente) {
        super(idUsuario, nome, documento, dataNascimento, telefone, tipoUsuario, senha);
        this.idCliente = idCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public Boolean login(String senha) {
        // Implement login logic here
        return null;
    }

    @Override
    public void logout() {
        // Implement logout logic here
    }

    @Override
    public String consultarDados() {
        // Implement data consultation logic here
        return "";
    }
}