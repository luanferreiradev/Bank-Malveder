package view;

import controler.Controler;
import model.dao.FuncionarioDAO;
import config.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AtualizarFuncionario extends JFrame {
    private Controler controler;
    private FuncionarioDAO funcionarioDAO;
    private Connection connection;
    private JTextField idFuncionarioField;
    private JTextField nomeField;
    private JTextField documentoField;
    private JTextField dataNascimentoField;
    private JTextField telefoneField;
    private JTextField cargoField;
    private JTextField cepField;
    private JTextField localField;
    private JTextField numeroCasaField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField senhaField;

    public AtualizarFuncionario(Controler controler) {
        this.controler = controler;
        try {
            this.connection = DatabaseConnection.getConnection();
            this.funcionarioDAO = new FuncionarioDAO(connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        setTitle("Atualizar Funcionário");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Add form fields
        formPanel.add(new JLabel("ID do Funcionário:"));
        idFuncionarioField = new JTextField();
        formPanel.add(idFuncionarioField);

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Documento:"));
        documentoField = new JTextField();
        formPanel.add(documentoField);

        formPanel.add(new JLabel("Data de Nascimento (YYYY-MM-DD):"));
        dataNascimentoField = new JTextField();
        formPanel.add(dataNascimentoField);

        formPanel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        formPanel.add(telefoneField);

        formPanel.add(new JLabel("Cargo:"));
        cargoField = new JTextField();
        formPanel.add(cargoField);

        formPanel.add(new JLabel("CEP:"));
        cepField = new JTextField();
        formPanel.add(cepField);

        formPanel.add(new JLabel("Local:"));
        localField = new JTextField();
        formPanel.add(localField);

        formPanel.add(new JLabel("Número da Casa:"));
        numeroCasaField = new JTextField();
        formPanel.add(numeroCasaField);

        formPanel.add(new JLabel("Bairro:"));
        bairroField = new JTextField();
        formPanel.add(bairroField);

        formPanel.add(new JLabel("Cidade:"));
        cidadeField = new JTextField();
        formPanel.add(cidadeField);

        formPanel.add(new JLabel("Estado:"));
        estadoField = new JTextField();
        formPanel.add(estadoField);

        formPanel.add(new JLabel("Senha:"));
        senhaField = new JTextField();
        formPanel.add(senhaField);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarFuncionario();
            }
        });
        formPanel.add(atualizarButton);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void atualizarFuncionario() {
        try {
            Integer idFuncionario = Integer.parseInt(idFuncionarioField.getText());
            String nome = nomeField.getText().isEmpty() ? null : nomeField.getText();
            String documento = documentoField.getText().isEmpty() ? null : documentoField.getText();
            String dataNascimentoStr = dataNascimentoField.getText().isEmpty() ? null : dataNascimentoField.getText();
            String telefone = telefoneField.getText().isEmpty() ? null : telefoneField.getText();
            String cargo = cargoField.getText().isEmpty() ? null : cargoField.getText();
            String cep = cepField.getText().isEmpty() ? null : cepField.getText();
            String local = localField.getText().isEmpty() ? null : localField.getText();
            Integer numeroCasa = numeroCasaField.getText().isEmpty() ? null : Integer.parseInt(numeroCasaField.getText());
            String bairro = bairroField.getText().isEmpty() ? null : bairroField.getText();
            String cidade = cidadeField.getText().isEmpty() ? null : cidadeField.getText();
            String estado = estadoField.getText().isEmpty() ? null : estadoField.getText();
            String senha = senhaField.getText().isEmpty() ? null : senhaField.getText();

            controler.atualizarFuncionario(idFuncionario, nome, documento, dataNascimentoStr, telefone, cargo, cep, local, numeroCasa, bairro, cidade, estado, senha);

            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar funcionário: " + ex.getMessage());
        }
    }
}