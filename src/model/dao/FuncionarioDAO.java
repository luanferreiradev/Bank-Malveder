package model.dao;

import config.DatabaseConnection;
import model.Conta;

import java.sql.*;
import java.util.Random;

// @autor: Luan Henrique de Souza Ferreira
public class FuncionarioDAO {
    private Connection connection;

    public FuncionarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void criarFuncionario(String nome, String documento, java.sql.Date dataNascimento, String telefone, String cargo, String codigoFuncionario, String senha, String cep, String local, int numeroCasa, String bairro, String cidade, String estado, double salario, String numeroConta) throws SQLException {
        String sql = "{CALL criarFuncionario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, documento);
            stmt.setDate(3, dataNascimento);
            stmt.setString(4, telefone);
            stmt.setString(5, cargo);
            stmt.setString(6, codigoFuncionario);
            stmt.setString(7, senha);
            stmt.setString(8, cep);
            stmt.setString(9, local);
            stmt.setInt(10, numeroCasa);
            stmt.setString(11, bairro);
            stmt.setString(12, cidade);
            stmt.setString(13, estado);
            stmt.setDouble(14, salario);
            stmt.setString(15, numeroConta);
            stmt.execute();
        }
    }

    public void deletarFuncionario(int idFuncionario) throws SQLException {
        String sql = "{CALL deletarFuncionario(?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, idFuncionario);
            stmt.execute();
        }
    }

    public ResultSet lerFuncionario(int idFuncionario) throws SQLException {
        String sql = "{CALL lerFuncionario(?)}";
        CallableStatement stmt = connection.prepareCall(sql);
        stmt.setInt(1, idFuncionario);
        return stmt.executeQuery();
    }

    public void atualizarFuncionario(int idFuncionario, String nome, String documento, java.sql.Date dataNascimento, String telefone, String cargo, String codigoFuncionario, String senha, String cep, String local, int numeroCasa, String bairro, String cidade, String estado) throws SQLException {
        String sql = "{CALL atualizarFuncionario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, idFuncionario);
            stmt.setString(2, nome);
            stmt.setString(3, documento);
            stmt.setDate(4, dataNascimento);
            stmt.setString(5, telefone);
            stmt.setString(6, cargo);
            stmt.setString(7, codigoFuncionario);
            stmt.setString(8, senha);
            stmt.setString(9, cep);
            stmt.setString(10, local);
            stmt.setInt(11, numeroCasa);
            stmt.setString(12, bairro);
            stmt.setString(13, cidade);
            stmt.setString(14, estado);
            stmt.execute();
        }
    }

    public String updateConta(int idUsuario, double limite, java.sql.Date dataVencimento, double rendimento, String tipoConta) throws SQLException {
        int idCliente = obterIdClientePorUsuario(idUsuario);
        int idConta = obterIdContaPorCliente(idCliente);
        String query = "UPDATE conta_corrente SET limite = ?, data_vencimento = ? WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, limite);
            stmt.setDate(2, dataVencimento);
            stmt.setInt(3, idConta);
            stmt.executeUpdate();
        }

        if ("POUPANCA".equals(tipoConta)) {
            query = "UPDATE conta_poupanca SET taxa_rendimento = ? WHERE id_conta = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setDouble(1, rendimento);
                stmt.setInt(2, idConta);
                stmt.executeUpdate();
            }
        }
        return "Sucesso";
    }


    public String alterarTipoConta(int idUsuario, String novoTipoConta, double novoLimite, java.sql.Date novaDataVencimento) throws SQLException {

        int idCliente = obterIdClientePorUsuario(idUsuario);
        int idConta = obterIdContaPorCliente(idCliente);
        String query = "UPDATE conta SET tipo_conta = ? WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoTipoConta);
            stmt.setInt(2, idConta);
            int rowsAffected = stmt.executeUpdate();
        }

        idCliente = obterIdClientePorUsuario(idUsuario);
        idConta = obterIdContaPorCliente(idCliente);
        if ("CORRENTE".equals(novoTipoConta)) {
            query = "UPDATE conta_corrente SET limite = ?, data_vencimento = ? WHERE id_conta = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setDouble(1, novoLimite);
                stmt.setDate(2, novaDataVencimento);
                stmt.setInt(3, idConta);
                stmt.executeUpdate();
            }
        }
        return "Sucesso";
    }

    public void atualizarConta(int idConta, String numeroConta, String agencia, double saldo, String tipoConta, String status) throws SQLException {
        // Verificar se a conta tem dívidas pendentes
        String query = "SELECT divida FROM conta_corrente WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double divida = rs.getDouble("divida");
                if (divida > 0) {
                    throw new SQLException("Não é possível alterar o tipo de conta com dívidas pendentes.");
                }
            }
        }


        // Chamar a procedure para alterar o tipo de conta
        String procedure = "{CALL alterar_tipo_conta(?, ?, ?, ?)}";
        try (PreparedStatement stmt = connection.prepareStatement(procedure)) {
            stmt.setInt(1, idConta);
            stmt.setString(2, tipoConta);
            stmt.setDouble(3, 0); // Novo limite (se aplicável)
            stmt.setDate(4, null); // Nova data de vencimento (se aplicável)
            stmt.execute();
        }
    }

    public void atualizarCliente(int idCliente, String nome, String documento, java.sql.Date dataNascimento, String telefone, String endereco, int numeroCasa, String bairro, String cidade, String estado, String tipoConta) throws SQLException {
        String sql = "{CALL atualizarCliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, nome);
            stmt.setString(3, documento);
            stmt.setDate(4, dataNascimento);
            stmt.setString(5, telefone);
            stmt.setString(6, endereco);
            stmt.setInt(7, numeroCasa);
            stmt.setString(8, bairro);
            stmt.setString(9, cidade);
            stmt.setString(10, estado);
            stmt.setString(11, tipoConta);
            stmt.execute();
        }
    }

    public ResultSet lerClienteCompleto(int idCliente) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT c.id_cliente, u.nome, u.documento, c.data_nascimento, u.telefone, " +
                "e.local AS endereco, e.numero_casa, e.bairro, e.cidade, e.estado, " +
                "co.tipo_conta, co.agencia, co.saldo, cc.limite, cc.data_vencimento, cc.divida, cp.taxa_rendimento " +
                "FROM cliente c " +
                "JOIN usuario u ON c.id_usuario = u.id_usuario " +
                "JOIN endereco e ON u.id_usuario = e.id_usuario " +
                "JOIN conta co ON c.id_cliente = co.id_cliente " +
                "LEFT JOIN conta_corrente cc ON co.id_conta = cc.id_conta " +
                "LEFT JOIN conta_poupanca cp ON co.id_conta = cp.id_conta " +
                "WHERE c.id_cliente = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, idCliente);
        return stmt.executeQuery();
    }

    // src/model/dao/FuncionarioDAO.java
    public ResultSet lerConta(int idConta) throws SQLException {
        String query = "SELECT c.*, cc.limite, cc.data_vencimento, cc.divida, cp.taxa_rendimento " +
                "FROM conta c " +
                "LEFT JOIN conta_corrente cc ON c.id_conta = cc.id_conta AND c.tipo_conta = 'CORRENTE' " +
                "LEFT JOIN conta_poupanca cp ON c.id_conta = cp.id_conta AND c.tipo_conta = 'POUPANCA' " +
                "WHERE c.id_conta = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, idConta);
        return stmt.executeQuery();
    }

    public double obterTaxaRendimento(int idUsuario) throws SQLException {
        int idCliente = obterIdClientePorUsuario(idUsuario);
        int idConta = obterIdContaPorCliente(idCliente);
        String query = "SELECT taxa_rendimento FROM conta_poupanca WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("taxa_rendimento");
            } else {
                throw new SQLException("Conta poupança não encontrada.");
            }
        }
    }

    public int obterIdClientePorUsuario(int idUsuario) throws SQLException {
        String query = "SELECT id_cliente FROM cliente WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_cliente");
            } else {
                throw new SQLException("Cliente não encontrado para o usuário: " + idUsuario);
            }
        }
    }

    public ResultSet verificarFuncionario(String codigoFuncionario, String senha) throws SQLException {
        String query = "SELECT * FROM funcionario f " +
                "JOIN usuario u ON f.id_usuario = u.id_usuario " +
                "WHERE f.codigo_funcionario = ? AND u.senha = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, codigoFuncionario);
        statement.setString(2, senha);
        return statement.executeQuery();
    }

    public int obterIdContaPorCliente(int idCliente) throws SQLException {
        String query = "SELECT id_conta FROM conta WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_conta");
            } else {
                throw new SQLException("Conta não encontrada para o cliente: " + idCliente);
            }
        }
    }

    private boolean codigoExiste(String codigoFuncionario) throws SQLException {
        String sql = "SELECT 1 FROM funcionario WHERE codigo_funcionario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoFuncionario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public String gerarCodigoFuncionario() throws SQLException {
        Random random = new Random();
        String codigoFuncionario = String.format("%05d", random.nextInt(100000));

        while (codigoExiste(codigoFuncionario)) {
            codigoFuncionario = String.format("%05d", random.nextInt(100000));
        }

        return codigoFuncionario;
    }

    public boolean verificarCodigoCadastrado(String codigoFuncionario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE codigo_funcionario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoFuncionario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                } else {
                    return false;
                }
            }
        }
    }

    public int obterIdFuncionarioPorCodigo(String codigoFuncionario) throws SQLException {
        String sql = "SELECT id_funcionario FROM funcionario WHERE codigo_funcionario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoFuncionario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_funcionario");
                } else {
                    throw new SQLException("Funcionário não encontrado para o código: " + codigoFuncionario);
                }
            }
        }
    }

    public int obterIdUsuarioPorFuncionario(int idFuncionario) throws SQLException {
        String sql = "SELECT id_usuario FROM funcionario WHERE id_funcionario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFuncionario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                } else {
                    throw new SQLException("Usuário não encontrado para o funcionário: " + idFuncionario);
                }
            }
        }
    }

    // Método para obter o id_usuario pelo codigo_funcionario
    public int obterIdUsuarioPorCodigoFuncionario(String codigoFuncionario) throws SQLException {
        String sql = "SELECT id_usuario FROM funcionario WHERE codigo_funcionario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoFuncionario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                } else {
                    System.err.println("Usuário não encontrado para o código do funcionário: " + codigoFuncionario);
                    throw new SQLException("Usuário não encontrado para o código do funcionário: " + codigoFuncionario);
                }
            }
        }
    }



    // Método para obter o nome do funcionário pelo id_usuario
    public String obterNomeFuncionario(String codigoFuncionario) throws SQLException {
        int idUsuario = obterIdUsuarioPorCodigoFuncionario(codigoFuncionario);
        String sql = "SELECT nome FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                } else {
                    throw new SQLException("Nome não encontrado para o id_usuario: " + idUsuario);
                }
            }
        }
    }

    // Método para obter o cargo do funcionário pelo codigo_funcionario
    public String getCargoFuncionario(String codigoFuncionario) throws SQLException {
        String sql = "SELECT cargo FROM funcionario WHERE codigo_funcionario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoFuncionario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cargo");
                } else {
                    throw new SQLException("Cargo não encontrado para o código do funcionário: " + codigoFuncionario);
                }
            }
        }
    }
}