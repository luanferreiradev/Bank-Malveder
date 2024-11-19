package model;

import java.time.LocalDate;

public abstract class Usuario {
    private Long id;
    private String nome;
    private LocalDate dataDeNascimento;
    private String telefone;
    private String senha;
    private Endereco endereco;

    public Usuario() {
    }

    public Usuario(String nome, LocalDate dataDeNascimento, String telefone, String senha, String cep, String local, String bairro, String cidade, String estado) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.telefone = telefone;
        this.senha = senha;
        this.endereco = new Endereco(cep, local, bairro, cidade, estado);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public abstract Boolean login(String senha);
    public abstract void logout();
    public abstract String consultarDados();
}