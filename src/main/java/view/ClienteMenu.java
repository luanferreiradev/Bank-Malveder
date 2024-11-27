package view;

import controler.Controler;
import model.dao.ContaDAO;
import config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class ClienteMenu extends JFrame {
    private Controler controler;
    private JLabel backButton;
    private JLabel titleLabel;
    private JLabel imageLabel;
    private JLabel accountInfoLabel;
    private JButton depositoButton;
    private JButton saqueButton;
    private JButton transferenciaButton;
    private JButton saldoButton;
    private JButton extratoButton;
    private JButton pagarDividaButton;
    private Connection connection;
    private String nome;
    private String tipoConta;
    private String numeroConta;

    // src/view/ClienteMenu.java

    public ClienteMenu(Controler controler, String nome, String tipoConta, String numeroConta, double saldo, double divida, String dataVencimento) {
        this.controler = controler;
        this.nome = nome;
        this.tipoConta = tipoConta;
        this.numeroConta = numeroConta;

        // Initialize the database connection
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        setTitle("Cliente Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        // Botão de voltar
        ImageIcon backIcon = new ImageIcon("/home/luanferreira/workspaces/ws-sts/BancoMalvaderNew/img/seta-esquerda.png");
        Image backImage = backIcon.getImage();
        Image scaledBackImage = backImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledBackIcon = new ImageIcon(scaledBackImage);
        JLabel backButton = new JLabel(scaledBackIcon);
        backButton.setBounds(10, 10, 50, 50);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Deseja fazer logout?", "Confirmar Logout", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    controler.logout();
                }
            }
        });
        add(backButton);



        // Title label
        titleLabel = new JLabel("BankMalvader");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(250, 50, 300, 50);
        add(titleLabel);

        // Image label
        ImageIcon originalIcon = new ImageIcon("/home/luanferreira/workspaces/ws-sts/BancoMalvaderNew/img/internet-banking.png");
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(600, 50, 150, 150);
        add(imageLabel);

        // Account info label
        accountInfoLabel = new JLabel();
        accountInfoLabel.setBounds(50, 150, 400, 150);
        add(accountInfoLabel);
        updateAccountInfoLabel();

        // Buttons
        depositoButton = new JButton("Depósito");
        depositoButton.setBounds(50, 350, 150, 50);
        depositoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String valorStr = JOptionPane.showInputDialog("Digite o valor do depósito:");
                try {
                    double valor = Double.parseDouble(valorStr);
                    controler.fazerDeposito(new ContaDAO(connection), valor);
                    updateAccountInfoLabel(); // Atualiza a label com o novo saldo
                    JOptionPane.showMessageDialog(null, "Depósito realizado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar depósito: " + ex.getMessage());
                }
            }
        });
        add(depositoButton);

        saqueButton = new JButton("Saque");
        saqueButton.setBounds(250, 350, 150, 50);
        saqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String valorStr = JOptionPane.showInputDialog("Digite o valor do saque:");
                try {
                    double valor = Double.parseDouble(valorStr);
                    controler.fazerSaque(new ContaDAO(connection), valor);
                    updateAccountInfoLabel(); // Atualiza a label com o novo saldo
                    JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar saque: " + ex.getMessage());
                }
            }
        });
        add(saqueButton);

        transferenciaButton = new JButton("Transferência");
        transferenciaButton.setBounds(450, 350, 150, 50);
        transferenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String documentoDestino = JOptionPane.showInputDialog("Digite o documento do destinatário:");
                String valorStr = JOptionPane.showInputDialog("Digite o valor da transferência:");
                try {
                    double valor = Double.parseDouble(valorStr);
                    controler.fazerTransferencia(new ContaDAO(connection), documentoDestino, valor);
                    updateAccountInfoLabel(); // Atualiza a label com o novo saldo
                    JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar transferência: " + ex.getMessage());
                }
            }
        });
        add(transferenciaButton);

        saldoButton = new JButton("Consultar Saldo");
        saldoButton.setBounds(50, 450, 150, 50);
        saldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String saldoInfo = controler.consultarSaldo(new ContaDAO(connection));
                    showResultInTextArea("Saldo", saldoInfo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao consultar saldo: " + ex.getMessage());
                }
            }
        });
        add(saldoButton);

        extratoButton = new JButton("Consultar Extrato");
        extratoButton.setBounds(250, 450, 150, 50);
        extratoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String extratoInfo = controler.consultarExtrato(new ContaDAO(connection));
                    showResultInTextArea("Extrato", extratoInfo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao consultar extrato: " + ex.getMessage());
                }
            }
        });
        add(extratoButton);

        if (tipoConta.equals("CORRENTE")) {
            pagarDividaButton = new JButton("Pagar Dívida");
            pagarDividaButton.setBounds(450, 450, 150, 50);
            pagarDividaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String valorStr = JOptionPane.showInputDialog("Digite o valor da dívida:");
                    try {
                        double valor = Double.parseDouble(valorStr);
                        controler.pagarDivida(new ContaDAO(connection), valor);
                        updateAccountInfoLabel(); // Atualiza a label com o novo saldo
                        JOptionPane.showMessageDialog(null, "Dívida paga com sucesso!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao pagar dívida: " + ex.getMessage());
                    }
                }
            });
            add(pagarDividaButton);
        }

        setVisible(true);
    }

    private void showResultInTextArea(String title, String result) {
        JFrame resultFrame = new JFrame(title);
        resultFrame.setSize(500, 400);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(result);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        resultFrame.add(scrollPane);
        resultFrame.setVisible(true);
    }

    private void updateAccountInfoLabel() {
        try {
            double saldoAtual = controler.getSaldo(controler.getDocumentoTemp());
            StringBuilder info = new StringBuilder();
            info.append("<html><div style='font-size:14px; line-height:1.5;'>");
            info.append("<b style='font-size:16px;'>Bem-vindo, ").append(nome).append("</b><br>");
            info.append("Tipo de Conta: ").append(tipoConta).append("<br>");
            info.append("Número da Conta: ").append(numeroConta).append("<br>");
            info.append("Saldo Atual: R$ ").append(saldoAtual).append("<br>");

            if ("CORRENTE".equals(tipoConta)) {
                double limite = controler.getLimite();
                double divida = controler.getDivida();
                String dataVencimento = controler.getDataVencimento();
                info.append("Limite: R$ ").append(limite).append("<br>");
                info.append("Dívida: R$ ").append(divida).append("<br>");
                info.append("Data de Vencimento: ").append(dataVencimento).append("<br>");
            }

            info.append("</div></html>");
            accountInfoLabel.setText(info.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar informações da conta: " + e.getMessage());
        }
    }
}