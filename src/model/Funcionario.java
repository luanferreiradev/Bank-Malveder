package model;

import java.time.LocalDate;


// Classe Funcionario
public class Funcionario extends Usuario {
    private int idFuncionario;
    private String codigoFuncionario;
    private String cargo;

    public Funcionario(int idUsuario, String nome, String documento, LocalDate dataNascimento, String telefone, String tipoUsuario, String senha, int idFuncionario, String codigoFuncionario, String cargo) {
        super(idUsuario, nome, documento, dataNascimento, telefone, tipoUsuario, senha);
        this.idFuncionario = idFuncionario;
        this.codigoFuncionario = codigoFuncionario;
        this.cargo = cargo;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getCodigoFuncionario() {
        return codigoFuncionario;
    }

    public void setCodigoFuncionario(String codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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

    public void criarConta(Conta conta) {
        // Implement account creation logic here
    }

    public Conta consultarDadosConta(Long idConta) {
        // Implement account data consultation logic here
        return null;
    }

    public void encerrarConta(Long idConta) {
        // Implement account closure logic here
    }

    public Cliente consultarDadosCliente(Long idCliente) {
        // Implement client data consultation logic here
        return null;
    }

    public void alterarDadosConta(Conta conta) {
        // Implement account data alteration logic here
    }

    public void alterarDadosCliente(Cliente cliente) {
        // Implement client data alteration logic here
    }

    public void cadastrarFuncionario(Funcionario funcionario) {
        // Implement employee registration logic here
    }

    public Relatorio gerarRelatorio(String tipoRelatorio, String conteudo) {
        // Implement report generation logic here
        return new Relatorio(tipoRelatorio, conteudo, this.idFuncionario);
    }
}