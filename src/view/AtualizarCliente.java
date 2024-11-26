package view;

import controler.Controler;
import model.dao.FuncionarioDAO;
import config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AtualizarCliente extends JFrame {
    private Controler controler;
    private FuncionarioDAO funcionarioDAO;
    private Connection connection;
    private JTextField idClienteField;
    private JTextField nomeField;
    private JTextField documentoField;
    private JTextField dataNascimentoField;
    private JTextField telefoneField;
    private JTextField enderecoField;
    private JTextField numeroCasaField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField tipoContaField;

    public AtualizarCliente(Controler controler) {
        this.controler = controler;
        try {
            this.connection = DatabaseConnection.getConnection();
            this.funcionarioDAO = new FuncionarioDAO(connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        setTitle("Atualizar Cliente");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(12, 2));

        // Add form fields
        add(new JLabel("ID do Cliente:"));
        idClienteField = new JTextField();
        add(idClienteField);

        add(new JLabel("Nome:"));
        nomeField = new JTextField();
        add(nomeField);

        add(new JLabel("Documento:"));
        documentoField = new JTextField();
        add(documentoField);

        add(new JLabel("Data de Nascimento (YYYY-MM-DD):"));
        dataNascimentoField = new JTextField();
        add(dataNascimentoField);

        add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        add(telefoneField);

        add(new JLabel("Endereço:"));
        enderecoField = new JTextField();
        add(enderecoField);

        add(new JLabel("Número da Casa:"));
        numeroCasaField = new JTextField();
        add(numeroCasaField);

        add(new JLabel("Bairro:"));
        bairroField = new JTextField();
        add(bairroField);

        add(new JLabel("Cidade:"));
        cidadeField = new JTextField();
        add(cidadeField);

        add(new JLabel("Estado:"));
        estadoField = new JTextField();
        add(estadoField);

        add(new JLabel("Tipo de Conta:"));
        tipoContaField = new JTextField();
        add(tipoContaField);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarCliente();
            }
        });
        add(atualizarButton);
    }

    private void atualizarCliente() {
        try {
            Integer idCliente = Integer.parseInt(idClienteField.getText());
            String nome = nomeField.getText().isEmpty() ? null : nomeField.getText();
            String documento = documentoField.getText().isEmpty() ? null : documentoField.getText();
            String dataNascimentoStr = dataNascimentoField.getText().isEmpty() ? null : dataNascimentoField.getText();
            String telefone = telefoneField.getText().isEmpty() ? null : telefoneField.getText();
            String endereco = enderecoField.getText().isEmpty() ? null : enderecoField.getText();
            Integer numeroCasa = numeroCasaField.getText().isEmpty() ? null : Integer.parseInt(numeroCasaField.getText());
            String bairro = bairroField.getText().isEmpty() ? null : bairroField.getText();
            String cidade = cidadeField.getText().isEmpty() ? null : cidadeField.getText();
            String estado = estadoField.getText().isEmpty() ? null : estadoField.getText();
            String tipoConta = tipoContaField.getText().isEmpty() ? null : tipoContaField.getText();

            controler.atualizarCliente(idCliente, nome, documento, dataNascimentoStr, telefone, endereco, numeroCasa, bairro, cidade, estado, tipoConta);

            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + ex.getMessage());
        }
    }
}