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

public class AtualizarConta extends JFrame {
    private Controler controler;
    private FuncionarioDAO funcionarioDAO;
    private ContaDAO contaDAO;
    private Connection connection;
    private JTextField idUsuarioField;
    private JTextField novoLimiteField;
    private JTextField novaDataVencimentoField;
    private JTextField novaTaxaRendimentoField;
    private JCheckBox alterarDadosContaCheckBox;
    private JCheckBox alterarTipoContaCheckBox;

    public AtualizarConta(Controler controler) {
        this.controler = controler;
        try {
            this.connection = DatabaseConnection.getConnection();
            this.funcionarioDAO = new FuncionarioDAO(connection);
            this.contaDAO = new ContaDAO(connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        setTitle("Atualizar Conta");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2));

        // Add form fields
        add(new JLabel("ID do Usuário:"));
        idUsuarioField = new JTextField();
        add(idUsuarioField);

        add(new JLabel("Novo Limite:"));
        novoLimiteField = new JTextField();
        add(novoLimiteField);

        add(new JLabel("Nova Data de Vencimento:"));
        novaDataVencimentoField = new JTextField();
        add(novaDataVencimentoField);

        add(new JLabel("Nova Taxa de Rendimento:"));
        novaTaxaRendimentoField = new JTextField();
        add(novaTaxaRendimentoField);

        alterarDadosContaCheckBox = new JCheckBox("Alterar Dados da Conta");
        add(alterarDadosContaCheckBox);

        alterarTipoContaCheckBox = new JCheckBox("Alterar Tipo de Conta");
        add(alterarTipoContaCheckBox);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarConta();
            }
        });
        add(atualizarButton);
    }

    private void atualizarConta() {
        try {
            Integer idUsuario = Integer.parseInt(idUsuarioField.getText());
            Boolean alterarDadosConta = alterarDadosContaCheckBox.isSelected();
            Boolean alterarTipoConta = alterarTipoContaCheckBox.isSelected();
            Double novoLimite = novoLimiteField.getText().isEmpty() ? 0 : Double.parseDouble(novoLimiteField.getText());
            String novaDataVencimentoStr = novaDataVencimentoField.getText().isEmpty() ? null : novaDataVencimentoField.getText();
            Double novaTaxaRendimento = novaTaxaRendimentoField.getText().isEmpty() ? 0 : Double.parseDouble(novaTaxaRendimentoField.getText());

            controler.atualizarConta(idUsuario, alterarDadosConta, alterarTipoConta, novoLimite, novaDataVencimentoStr, novaTaxaRendimento);

            JOptionPane.showMessageDialog(this, "Conta atualizada com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar conta: " + ex.getMessage());
        }
    }
}