package model.dao;

import java.sql.*;

// Classe que realiza operações no banco de dados relacionadas a solicitações
public class SolicitacaoDAO {
    private Connection connection;

    public SolicitacaoDAO(Connection connection) {
        this.connection = connection;
    }

    public ResultSet listarSolicitacoes() throws SQLException {
        String sql = "SELECT id_solicitacao, documento, senha, is_pessoa_fisica, endereco, numero_casa, bairro, cidade, estado, data_nascimento, nome, telefone, tipo_conta, id_status, status, numero_conta, agencia, saldo FROM solicitacao";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeQuery();
    }


    public void rejeitarSolicitacao(int idSolicitacao) throws SQLException {
        String sql = "{CALL rejeitarSolicitacao(?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, idSolicitacao);
            stmt.execute();
        }
    }

    public void aprovarSolicitacao(int idSolicitacao) throws SQLException {
        String sql = "CALL aprovarSolicitacao(?)";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, idSolicitacao);
            stmt.execute();
        }
    }


    public void criarSolicitacao(String nome, String documento, java.sql.Date dataNascimento, String telefone, String tipoUsuario, String senha, boolean isPessoaFisica, String endereco, int numeroCasa, String bairro, String cidade, String estado, String numeroConta, String agencia, double saldo, String tipoConta) throws SQLException {
        String sql = "INSERT INTO solicitacao (nome, documento, data_nascimento, telefone, tipo_usuario, senha, is_pessoa_fisica, endereco, numero_casa, bairro, cidade, estado, numero_conta, agencia, saldo, tipo_conta, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Pendente')";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, nome);
        statement.setString(2, documento);
        statement.setDate(3, dataNascimento);
        statement.setString(4, telefone);
        statement.setString(5, tipoUsuario);
        statement.setString(6, senha);
        statement.setBoolean(7, isPessoaFisica);
        statement.setString(8, endereco);
        statement.setInt(9, numeroCasa);
        statement.setString(10, bairro);
        statement.setString(11, cidade);
        statement.setString(12, estado);
        statement.setString(13, numeroConta);
        statement.setString(14, agencia);
        statement.setDouble(15, saldo);
        statement.setString(16, tipoConta);
        statement.executeUpdate();
    }

    public void excluirSolicitacao(int idSolicitacao) throws SQLException {
        String sql = "{CALL excluirSolicitacao(?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, idSolicitacao);
            stmt.execute();
        }
    }
}