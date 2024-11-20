package model.dao.implDAO;

import model.Cliente;
import model.enums.Status_Solicitacao;
import model.impl.ContaPoupanca;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContaPoupancaDAO {

    public ContaPoupanca getContaPoupanca(String numeroConta) {
        ContaPoupanca contaPoupanca = null;
        String sql = "SELECT * FROM conta_poupanca WHERE numero_conta = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroConta);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Cliente cliente = getClienteById(resultSet.getLong("id_cliente"));
                contaPoupanca = new ContaPoupanca(
                        resultSet.getLong("id_cliente"), // Adiciona o idCliente
                        cliente,
                        resultSet.getLong("numero_conta"),
                        resultSet.getString("endereco"),
                        resultSet.getString("cidade"),
                        resultSet.getString("tipo_conta")
                );
                contaPoupanca.setTaxaRendimento(resultSet.getDouble("taxa_rendimento"));
                contaPoupanca.setSaldo(resultSet.getDouble("saldo"));
                contaPoupanca.setStatus(Status_Solicitacao.valueOf(resultSet.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contaPoupanca;
    }

    private Cliente getClienteById(long idCliente) {
        Cliente cliente = null;
        String sql = "SELECT nome, documento, is_pessoa_fisica, telefone, senha, cep, local, numero_casa, bairro, cidade, estado, data_nascimento " +
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

    // Implementar outros métodos como criar, atualizar e remover ContaPoupanca conforme necessário
}