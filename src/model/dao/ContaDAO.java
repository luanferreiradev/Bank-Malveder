package model.dao;

import model.service.ExtratoVO;
import model.service.TransacaoVO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// @autor: Luan Henrique de Souza Ferreira
public class ContaDAO {
    private Connection connection;

    public ContaDAO(Connection connection) {
        this.connection = connection;
    }

    public void depositar(int idConta, double valor) throws SQLException {
        String sql = "UPDATE conta SET saldo = saldo + ? WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, valor);
            stmt.setInt(2, idConta);
            stmt.executeUpdate();
        }
    }
    public boolean verificarDivida(int idConta) throws SQLException {
        String query = "SELECT divida FROM conta_corrente WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double divida = rs.getDouble("divida");
                return divida > 0;
            }
        }
        return false;
    }

    public void pagarDivida(int idConta, double valor) throws SQLException {
        // Verificar o valor da dívida atual na tabela conta_corrente
        String sqlSelectDivida = "SELECT divida FROM conta_corrente WHERE id_conta = ?";
        PreparedStatement stmtSelectDivida = connection.prepareStatement(sqlSelectDivida);
        stmtSelectDivida.setInt(1, idConta);
        ResultSet rsDivida = stmtSelectDivida.executeQuery();

        if (rsDivida.next()) {
            double dividaAtual = rsDivida.getDouble("divida");

            // Verificar se a dívida é menor que zero (não deve ser permitida)
            if (dividaAtual < 0) {
                throw new SQLException("A dívida não pode ser negativa.");
            }

            // Se o valor do pagamento for maior que a dívida atual, lançar uma exceção
            if (valor > dividaAtual) {
                throw new SQLException("O valor do pagamento não pode ser maior que a dívida atual.");
            }

            // Reduzir a dívida pelo valor pago
            double novaDivida = dividaAtual - valor;

            // Verificar se a nova dívida é negativa (não deve permitir)
            if (novaDivida < 0) {
                throw new SQLException("A operação resultaria em uma dívida negativa.");
            }

            // Registrar a transação no extrato usando a stored procedure
            registrarTransacao("PAGAR_DIVIDA", BigDecimal.valueOf(valor), idConta, "APROVADO", null);
        } else {
            throw new SQLException("Conta não encontrada.");
        }
    }

    public void sacar(int idConta, double valor) throws SQLException {
        String tipoConta;
        double saldo;
        double limite = 0;
        double divida = 0;

        // Obter o tipo de conta e saldo
        String sqlObterConta = "SELECT c.tipo_conta, c.saldo, cc.divida, cc.limite " +
                "FROM conta c " +
                "LEFT JOIN conta_corrente cc ON c.id_conta = cc.id_conta " +
                "WHERE c.id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlObterConta)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tipoConta = rs.getString("tipo_conta");
                    saldo = rs.getDouble("saldo");
                    limite = rs.getDouble("limite");
                    divida = rs.getDouble("divida");
                } else {
                    throw new SQLException("Conta não encontrada.");
                }
            }
        }

        if (tipoConta.equals("POUPANCA")) {
            if (saldo < valor) {
                throw new SQLException("Saldo insuficiente.");
            } else {
                String sqlAtualizarSaldo = "UPDATE conta SET saldo = saldo - ? WHERE id_conta = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlAtualizarSaldo)) {
                    stmt.setDouble(1, valor);
                    stmt.setInt(2, idConta);
                    stmt.executeUpdate();
                }
            }
        } else if (tipoConta.equals("CORRENTE")) {
            if (saldo >= valor) {
                // Saldo suficiente
                String sqlAtualizarSaldo = "UPDATE conta SET saldo = saldo - ? WHERE id_conta = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlAtualizarSaldo)) {
                    stmt.setDouble(1, valor);
                    stmt.setInt(2, idConta);
                    stmt.executeUpdate();
                }
                zerarSaldo(idConta); // Zerar saldo da conta
            } else if (saldo + limite >= valor) {
                // Saldo suficiente com limite
                double valorDivida = valor - saldo;
                double novoLimite = limite - valorDivida;

                String sqlAtualizarContaCorrente = "UPDATE conta_corrente SET limite = ?, divida = ? WHERE id_conta = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlAtualizarContaCorrente)) {
                    stmt.setDouble(1, novoLimite);
                    stmt.setDouble(2, divida + valorDivida);
                    stmt.setInt(3, idConta);
                    stmt.executeUpdate();
                }

            } else {
                throw new SQLException("Saldo insuficiente.");
            }
        } else {
            throw new SQLException("Tipo de conta inválido.");
        }

        registrarTransacao("SAQUE", BigDecimal.valueOf(valor), idConta, "APROVADO", null);
    }
    public void zerarSaldo(int idConta) throws SQLException {
        String sql = "UPDATE conta SET saldo = 0 WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Saldo zerado com sucesso para a conta: " + idConta);
            } else {
                System.out.println("Nenhuma conta encontrada para zerar o saldo: " + idConta);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao zerar o saldo: " + e.getMessage());
            throw e;
        }
    }



    public void transferir(int idContaOrigem, int idContaDestino, double valor) throws SQLException {
        String sql = "{CALL transferir(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, idContaOrigem);
            stmt.setInt(2, idContaDestino);
            stmt.setDouble(3, valor);
            stmt.execute();
            registrarTransacao("TRANSFERENCIA", BigDecimal.valueOf(valor), idContaOrigem, "APROVADO", idContaDestino);
        }
    }
    public String verSaldo(int idConta) throws SQLException {
        String sql = "SELECT c.tipo_conta, c.saldo, cc.divida, cc.data_vencimento " +
                "FROM conta c " +
                "LEFT JOIN conta_corrente cc ON c.id_conta = cc.id_conta " +
                "WHERE c.id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipoConta = rs.getString("tipo_conta");
                    double saldo = rs.getDouble("saldo");
                    if ("CORRENTE".equals(tipoConta)) {
                        double divida = rs.getDouble("divida");
                        Date dataVencimento = rs.getDate("data_vencimento");
                        String dataVencimentoStr = (dataVencimento != null) ? dataVencimento.toString() : "N/A";
                        return String.format("Saldo: %.2f, Dívida: %.2f, Data de Vencimento: %s", saldo, divida, dataVencimentoStr);
                    } else if ("POUPANCA".equals(tipoConta)) {
                        double taxaRendimento = obterTaxaRendimento(idConta);
                        return String.format("Saldo: %.2f, Taxa de Rendimento: %.2f", saldo, taxaRendimento);
                    } else {
                        return "Tipo de conta desconhecido";
                    }
                }
            }
        }
        return "Conta não encontrada";
    }

    public Double getDivida(int idConta) throws SQLException {
        String sql = "SELECT divida FROM conta_corrente WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("divida");
                } else {
                    throw new SQLException("Conta corrente não encontrada.");
                }
            }
        }
    }

    public String getDataVencimento(int idConta) throws SQLException {
        String sql = "SELECT data_vencimento FROM conta_corrente WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date dataVencimento = rs.getDate("data_vencimento");
                    return dataVencimento.toString();
                } else {
                    throw new SQLException("Conta corrente não encontrada.");
                }
            }
        }
    }

    public Double getLimite(int idConta) throws SQLException {
        String sql = "SELECT limite FROM conta_corrente WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("limite");
                } else {
                    throw new SQLException("Conta corrente não encontrada.");
                }
            }
        }
    }

    private double obterTaxaRendimento(int idConta) throws SQLException {
        String sql = "SELECT taxa_rendimento FROM conta_poupanca WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("taxa_rendimento");
                } else {
                    throw new SQLException("Conta poupança não encontrada.");
                }
            }
        }
    }

    public void registrarTransacao(String tipoTransacao, BigDecimal valor, int idConta, String status, Integer idContaDestino) throws SQLException {
        String sql = "CALL registrarTransacao(?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoTransacao);
            stmt.setBigDecimal(2, valor);
            stmt.setInt(3, idConta);
            stmt.setString(4, status);
            if (idContaDestino == null) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, idContaDestino);
            }

            stmt.execute();
        } catch (SQLException e) {
            // Log ou tratar a exceção conforme necessário
            throw new SQLException("Erro ao registrar a transação: " + e.getMessage(), e);
        }
    }

    public int obterIdContaPorDocumento(String documento) throws SQLException {
        String sql = "{CALL obterIdContaPorDocumento(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, documento);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(2);
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

    public int obterIdContaPorCliente(int idCliente) throws SQLException {
        String sql = "SELECT id_conta FROM conta WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_conta");
                } else {
                    throw new SQLException("Conta não encontrada para o cliente com ID: " + idCliente);
                }
            }
        }
    }

    public List<ExtratoVO> consultarExtrato(int idConta) throws SQLException {
        String sql = "SELECT u.nome, u.documento, co.numero_conta, t.tipo_transacao, t.valor, t.data_hora " +
                "FROM transacao t " +
                "JOIN conta co ON t.id_conta = co.id_conta " +
                "JOIN cliente cl ON co.id_cliente = cl.id_cliente " +
                "JOIN usuario u ON cl.id_usuario = u.id_usuario " +
                "WHERE co.id_conta = ?";
        List<ExtratoVO> extrato = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String documento = rs.getString("documento");
                    String numeroConta = rs.getString("numero_conta");
                    String tipoTransacao = rs.getString("tipo_transacao");
                    BigDecimal valor = rs.getBigDecimal("valor");
                    Timestamp dataHora = rs.getTimestamp("data_hora");

                    TransacaoVO transacao = new TransacaoVO(tipoTransacao, valor, dataHora.toLocalDateTime(), null);
                    ExtratoVO extratoVO = new ExtratoVO(nome, documento, numeroConta, transacao);
                    extrato.add(extratoVO);
                }
            }
        }
        return extrato;
    }

    public String obterTipoContaPorId(int idConta) throws SQLException {
        String sql = "SELECT tipo_conta FROM conta WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tipo_conta");
                } else {
                    throw new SQLException("Conta não encontrada para o ID fornecido");
                }
            }
        }
    }

    public String obterNumeroContaPorDocumento(String documento) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

        // Step 1: Obtain the user ID using the document
        int idUsuario = usuarioDAO.obterIdUsuarioPorDocumento(documento);

        // Step 2: Obtain the client ID using the user ID
        int idCliente = usuarioDAO.obterIdClientePorUsuario(idUsuario);

        // Step 3: Obtain the account ID using the client ID
        int idConta = obterIdContaPorCliente(idCliente);

        // Step 4: Retrieve the account number using the account ID
        String sql = "SELECT numero_conta FROM conta WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("numero_conta");
                } else {
                    throw new SQLException("Conta não encontrada para o cliente com ID: " + idCliente);
                }
            }
        }
    }

    public double obterSaldoPorDocumento(String documento) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

        // Step 1: Obtain the user ID using the document
        int idUsuario = usuarioDAO.obterIdUsuarioPorDocumento(documento);

        // Step 2: Obtain the client ID using the user ID
        int idCliente = usuarioDAO.obterIdClientePorUsuario(idUsuario);

        // Step 3: Obtain the account ID using the client ID
        int idConta = obterIdContaPorCliente(idCliente);

        // Step 4: Retrieve the account balance using the account ID
        String sql = "SELECT saldo FROM conta WHERE id_conta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                } else {
                    throw new SQLException("Conta não encontrada para o cliente com ID: " + idCliente);
                }
            }
        }
    }
}