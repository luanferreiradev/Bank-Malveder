package model;

import java.time.LocalDate;

// Classe abstrata Usuario
public abstract class Usuario {
    private int idUsuario;
    private String nome;
    private String documento;
    private LocalDate dataNascimento;
    private String telefone;
    private String tipoUsuario;
    private String senha;

    public Usuario(int idUsuario, String nome, String documento, LocalDate dataNascimento, String telefone, String tipoUsuario, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.documento = documento;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.senha = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public abstract Boolean login(String senha);
    public abstract void logout();
    public abstract String consultarDados();
}