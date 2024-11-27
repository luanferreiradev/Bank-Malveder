package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;


// @autor: Luan Henrique de Souza Ferreira
public class ClienteDAO {
    private Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void atualizarCliente(int idCliente, String nome, String documento, Date dataNascimento, String telefone, String endereco, int numeroCasa, String bairro, String cidade, String estado, String tipoConta) throws SQLException {
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


    public ResultSet lerCliente(int idCliente) throws SQLException {
        String sql = "{CALL lerCliente(?)}";
        CallableStatement stmt = connection.prepareCall(sql);
        stmt.setInt(1, idCliente);
        return stmt.executeQuery();
    }
}