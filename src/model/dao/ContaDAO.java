package model.dao;

import model.Cliente;
import model.Conta;
import model.enums.Status_Solicitacao;
import model.impl.ContaCorrente;
import model.impl.ContaPoupanca;
import util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContaDAO {

    public Conta getConta(String numeroConta) {
        Conta conta = null;
        String sql = "SELECT * FROM conta WHERE numero_conta = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroConta);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String tipoConta = resultSet.getString("tipo_conta");
                Cliente cliente = getClienteById(resultSet.getLong("id_cliente"));
                Long idCliente = resultSet.getLong("id_cliente");
                if ("CORRENTE".equals(tipoConta)) {
                    conta = new ContaCorrente(
                            idCliente, // Passa o idCliente
                            cliente,
                            resultSet.getLong("numero_conta"),
                            resultSet.getString("endereco"),
                            resultSet.getString("cidade"),
                            tipoConta
                    );
                    ((ContaCorrente) conta).setLimite(resultSet.getBigDecimal("limite").doubleValue());
                    ((ContaCorrente) conta).setDivida(resultSet.getBigDecimal("divida").doubleValue());
                } else if ("POUPANCA".equals(tipoConta)) {
                    conta = new ContaPoupanca(
                            idCliente, // Passa o idCliente
                            cliente,
                            resultSet.getLong("numero_conta"),
                            resultSet.getString("endereco"),
                            resultSet.getString("cidade"),
                            tipoConta
                    );
                    ((ContaPoupanca) conta).setTaxaRendimento(resultSet.getBigDecimal("taxa_rendimento").doubleValue());
                }
                conta.setSaldo(resultSet.getBigDecimal("saldo").doubleValue());
                conta.setStatus(Status_Solicitacao.valueOf(resultSet.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conta;
    }

    private Cliente getClienteById(long idCliente) {
        Cliente cliente = null;
        String sql = "SELECT u.nome, u.documento, u.data_nascimento, u.telefone, u.senha, e.cep, e.local, e.numero_casa, e.bairro, e.cidade, e.estado, u.is_pessoa_fisica " +
                "FROM cliente c " +
                "JOIN usuario u ON c.id_usuario = u.id_usuario " +
                "JOIN endereco e ON u.id_usuario = e.id_usuario " +
                "WHERE c.id_cliente = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, idCliente);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                cliente = new Cliente(
                        resultSet.getString("documento"),
                        resultSet.getString("senha"),
                        resultSet.getBoolean("is_pessoa_fisica"),
                        resultSet.getString("local"),
                        resultSet.getString("numero_casa"),
                        resultSet.getString("bairro"),
                        resultSet.getString("cidade"),
                        resultSet.getString("estado"),
                        resultSet.getDate("data_nascimento").toLocalDate().toString(),
                        resultSet.getString("nome"),
                        resultSet.getString("telefone")
                );
                cliente.setId(idCliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    public void criarConta(Conta conta) {
        String sql = "CALL criarConta(?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, conta.getNumeroConta().toString());
            statement.setString(2, conta.getAgencia());
            statement.setBigDecimal(3, BigDecimal.valueOf(conta.consultaSaldo()));
            statement.setString(4, conta.getTipoConta());
            statement.setString(5, conta.getStatus().toString());
            statement.setString(6, conta.getCliente().getDocumento());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerConta(String numeroConta) {
        String sql = "CALL removerConta(?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroConta);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposito(String numeroConta, BigDecimal valor) {
        String sql = "CALL deposito(?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroConta);
            statement.setBigDecimal(2, valor);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saque(String numeroConta, BigDecimal valor) {
        String sql = "CALL saque(?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroConta);
            statement.setBigDecimal(2, valor);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transferencia(String numeroContaOrigem, String numeroContaDestino, BigDecimal valor) {
        String sql = "CALL transferencia(?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroContaOrigem);
            statement.setString(2, numeroContaDestino);
            statement.setBigDecimal(3, valor);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal verSaldo(String numeroConta) {
        BigDecimal saldo = null;
        String sql = "CALL verSaldo(?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroConta);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                saldo = resultSet.getBigDecimal("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return saldo;
    }
}