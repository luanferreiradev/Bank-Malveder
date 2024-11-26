package model.dao;

import java.sql.*;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void criarUsuario(String nome, String documento, java.sql.Date dataNascimento, String telefone, String tipoUsuario, String senha, boolean isPessoaFisica, String endereco, int numeroCasa, String bairro, String cidade, String estado, String numeroConta, String agencia, double saldo, String tipoConta, String status) throws SQLException {
        String sql = "{CALL criarUsuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, documento);
            stmt.setDate(3, dataNascimento);
            stmt.setString(4, telefone);
            stmt.setString(5, tipoUsuario);
            stmt.setString(6, senha);
            stmt.setBoolean(7, isPessoaFisica);
            stmt.setString(8, endereco);
            stmt.setInt(9, numeroCasa);
            stmt.setString(10, bairro);
            stmt.setString(11, cidade);
            stmt.setString(12, estado);
            stmt.setString(13, numeroConta);
            stmt.setString(14, agencia);
            stmt.setDouble(15, saldo);
            stmt.setString(16, tipoConta);
            stmt.setString(17, status);
            stmt.execute();
        }
    }

    public void deletarUsuario(int idUsuario) throws SQLException {
        // Remover registros relacionados em transacao
        String sqlDeleteTransacao = "DELETE FROM transacao WHERE id_conta IN (SELECT id_conta FROM conta WHERE id_cliente IN (SELECT id_cliente FROM cliente WHERE id_usuario = ?))";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteTransacao)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }

        // Remover registros relacionados em endereco
        String sqlDeleteEndereco = "DELETE FROM endereco WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteEndereco)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }

        // Remover registros relacionados em cliente_pf
        String sqlDeleteClientePF = "DELETE FROM cliente_pf WHERE id_cliente IN (SELECT id_cliente FROM cliente WHERE id_usuario = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteClientePF)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }

        // Remover registros relacionados em conta_poupanca
        String sqlDeleteContaPoupanca = "DELETE FROM conta_poupanca WHERE id_conta IN (SELECT id_conta FROM conta WHERE id_cliente IN (SELECT id_cliente FROM cliente WHERE id_usuario = ?))";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteContaPoupanca)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }

        // Remover registros relacionados em conta_corrente
        String sqlDeleteContaCorrente = "DELETE FROM conta_corrente WHERE id_conta IN (SELECT id_conta FROM conta WHERE id_cliente IN (SELECT id_cliente FROM cliente WHERE id_usuario = ?))";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteContaCorrente)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }

        // Remover registros relacionados em conta
        String sqlDeleteConta = "DELETE FROM conta WHERE id_cliente IN (SELECT id_cliente FROM cliente WHERE id_usuario = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteConta)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }

        // Remover registros relacionados em cliente
        String sqlDeleteCliente = "DELETE FROM cliente WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteCliente)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }

        // Remover o usuário
        String sqlDeleteUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteUsuario)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }
    }

    public ResultSet verUsuario(int idUsuario) throws SQLException {
        String sql = "{CALL verUsuario(?)}";
        CallableStatement stmt = connection.prepareCall(sql);
        stmt.setInt(1, idUsuario);
        return stmt.executeQuery();
    }

    public int obterIdUsuarioPorDocumento(String documento) throws SQLException {
        String sql = "{CALL obterIdUsuarioPorDocumento(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, documento);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(2);
        }
    }

    // src/model/dao/UsuarioDAO.java

    public int obterIdClientePorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT id_cliente FROM cliente WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_cliente");
                } else {
                    throw new SQLException("Cliente não encontrado para o usuário com ID: " + idUsuario);
                }
            }
        }
    }

    public boolean verificarDocumentoCadastrado(String documento) throws SQLException {
        String sql = "{CALL verificarDocumentoCadastrado(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, documento);
            stmt.registerOutParameter(2, Types.BOOLEAN);
            stmt.execute();
            return stmt.getBoolean(2);
        }
    }

    public boolean verificarSenha(int idUsuario, String senha) throws SQLException {
        String sql = "SELECT senha FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String senhaBanco = rs.getString("senha");
                    return senhaBanco.equals(senha);
                } else {
                    return false;
                }
            }
        }
    }

    public String getNomeTitular(String documento) throws SQLException {
        String sql = "SELECT nome FROM usuario WHERE documento = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, documento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                } else {
                    return null; // Documento não encontrado
                }
            }
        }
    }
}