// src/view/CadastroFuncionario.java
package view;

import controler.Controler;
import model.dao.ContaDAO;
import model.dao.FuncionarioDAO;
import config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class CadastroFuncionario extends JFrame {
    private Controler controler;
    private FuncionarioDAO funcionarioDAO;
    private ContaDAO contaDAO;
    private Connection connection;
    private JTextField nomeField;
    private JTextField documentoField;
    private JTextField dataNascimentoField;
    private JTextField telefoneField;
    private JTextField cargoField;
    private JPasswordField senhaField;
    private JTextField cepField;
    private JTextField localField;
    private JTextField numeroCasaField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField salarioField;

    public CadastroFuncionario(Controler controler) {
        this.controler = controler;
        try {
            this.connection = DatabaseConnection.getConnection();
            this.funcionarioDAO = new FuncionarioDAO(connection);
            this.contaDAO = new ContaDAO(connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        setTitle("Cadastro de Funcionário");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(15, 2));

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

        add(new JLabel("Cargo:"));
        cargoField = new JTextField();
        add(cargoField);

        add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        add(senhaField);

        add(new JLabel("CEP:"));
        cepField = new JTextField();
        add(cepField);

        add(new JLabel("Local:"));
        localField = new JTextField();
        add(localField);

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

        add(new JLabel("Salário:"));
        salarioField = new JTextField();
        add(salarioField);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarFuncionario();
            }
        });
        add(cadastrarButton);
    }

    private void cadastrarFuncionario() {
        try {
            String nome = nomeField.getText();
            String documento = documentoField.getText();
            String dataNascimentoStr = dataNascimentoField.getText();
            String telefone = telefoneField.getText();
            String cargo = cargoField.getText();
            String senha = new String(senhaField.getPassword());
            String cep = cepField.getText();
            String local = localField.getText();
            int numeroCasa = Integer.parseInt(numeroCasaField.getText());
            String bairro = bairroField.getText();
            String cidade = cidadeField.getText();
            String estado = estadoField.getText();
            double salario = Double.parseDouble(salarioField.getText());

            // Chamando o método do controlador com os argumentos apropriados
            controler.criarFuncionario(
                    nome, documento, dataNascimentoStr, telefone, cargo, senha, cep, local, numeroCasa, bairro, cidade, estado, salario
            );

            JOptionPane.showMessageDialog(this, "Funcionário criado com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar funcionário: " + ex.getMessage());
        }
    }
}