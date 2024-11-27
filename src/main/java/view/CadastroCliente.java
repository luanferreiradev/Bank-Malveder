// src/view/CadastroCliente.java
package view;

import controler.Controler;
import model.dao.ContaDAO;
import model.dao.SolicitacaoDAO;
import model.dao.UsuarioDAO;
import config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class CadastroCliente extends JFrame {
    private Controler controler;
    private SolicitacaoDAO solicitacaoDAO;
    private UsuarioDAO usuarioDAO;
    private ContaDAO contaDAO;
    private Connection connection;
    private JTextField nomeField;
    private JTextField documentoField;
    private JTextField dataNascimentoField;
    private JTextField telefoneField;
    private JPasswordField senhaField;
    private JTextField enderecoField;
    private JTextField numeroCasaField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JComboBox<String> tipoContaComboBox;

    public CadastroCliente(Controler controler) {
        this.controler = controler;
        try {
            this.connection = DatabaseConnection.getConnection();
            this.solicitacaoDAO = new SolicitacaoDAO(connection);
            this.usuarioDAO = new UsuarioDAO(connection);
            this.contaDAO = new ContaDAO(connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        setTitle("Cadastro de Cliente");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(13, 2));

        // Add form fields
        add(new JLabel("Nome:"));
        nomeField = new JTextField();
        add(nomeField);

        add(new JLabel("Documento:"));
        documentoField = new JTextField();
        add(documentoField);

        add(new JLabel("Data de Nascimento:"));
        dataNascimentoField = new JTextField();
        add(dataNascimentoField);

        add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        add(telefoneField);

        add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        add(senhaField);

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
        tipoContaComboBox = new JComboBox<>(new String[]{"CORRENTE", "POUPANCA"});
        add(tipoContaComboBox);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });
        add(cadastrarButton);
    }

    private void cadastrarCliente() {
        try {
            String nome = nomeField.getText();
            String documento = documentoField.getText();
            String dataNascimento = dataNascimentoField.getText();
            String telefone = telefoneField.getText();
            String senha = new String(senhaField.getPassword());
            String endereco = enderecoField.getText();
            int numeroCasa = Integer.parseInt(numeroCasaField.getText());
            String bairro = bairroField.getText();
            String cidade = cidadeField.getText();
            String estado = estadoField.getText();
            int tipoContaOpcao = tipoContaComboBox.getSelectedItem().equals("CORRENTE") ? 1 : 2;

            // Chamando o método do controlador com os argumentos apropriados
            controler.criarSolicitacao(
                    solicitacaoDAO, usuarioDAO, contaDAO,
                    nome, documento, dataNascimento, telefone, senha, true,
                    endereco, numeroCasa, bairro, cidade, estado, tipoContaOpcao
            );

            JOptionPane.showMessageDialog(this, "Solicitação de cadastro enviada!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro Solicitação de cadastro do cliente: " + ex.getMessage());
        }
    }
}