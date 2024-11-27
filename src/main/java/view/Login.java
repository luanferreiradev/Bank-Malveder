package view;

import controler.Controler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Login extends JFrame {
    private Controler controler;
    private JButton clienteButton;
    private JButton funcionarioButton;
    private JButton loginClienteButton;
    private JButton cadastrarClienteButton; // New button for registering a new client
    private JButton loginFuncionarioButton;
    private JButton sairButton; // New button for exiting the application
    private JButton voltarButton; // New button for going back to the main panel
    private JLabel statusLabel;
    private JLabel titleLabel;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JTextField documentoField;
    private JPasswordField senhaField;

    public Login() {
        controler = new Controler();

        setTitle("Login");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

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
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(600, 50, 150, 150);
        add(imageLabel);

        // Main panel with buttons
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 800, 600);

        clienteButton = new JButton("Cliente");
        clienteButton.setBounds(300, 200, 200, 50);
        clienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginForm(false);
            }
        });
        mainPanel.add(clienteButton);

        funcionarioButton = new JButton("Funcionário");
        funcionarioButton.setBounds(300, 300, 200, 50);
        funcionarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginForm(true);
            }
        });
        mainPanel.add(funcionarioButton);

        // Sair button
        sairButton = new JButton("Sair");
        sairButton.setBounds(300, 400, 200, 50);
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mainPanel.add(sairButton);

        add(mainPanel);

        // Form panel for login
        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBounds(0, 0, 800, 600);

        documentoField = new JTextField();
        documentoField.setBounds(300, 200, 200, 30);
        JLabel documentoLabel = new JLabel("Documento:");
        documentoLabel.setBounds(200, 200, 100, 30);
        formPanel.add(documentoLabel);
        formPanel.add(documentoField);

        senhaField = new JPasswordField();
        senhaField.setBounds(300, 250, 200, 30);
        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setBounds(200, 250, 100, 30);
        formPanel.add(senhaLabel);
        formPanel.add(senhaField);

        loginClienteButton = new JButton("Login Cliente");
        loginClienteButton.setBounds(300, 300, 200, 50);
        loginClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(false);
            }
        });
        formPanel.add(loginClienteButton);

        cadastrarClienteButton = new JButton("Cadastrar Cliente"); // New button for registering a new client
        cadastrarClienteButton.setBounds(300, 360, 200, 50);
        cadastrarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CadastroCliente(controler).setVisible(true); // Open the CadastroCliente form
            }
        });
        formPanel.add(cadastrarClienteButton);

        loginFuncionarioButton = new JButton("Login Funcionário");
        loginFuncionarioButton.setBounds(300, 300, 200, 50);
        loginFuncionarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(true);
            }
        });
        formPanel.add(loginFuncionarioButton);

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
                mainPanel.setVisible(true);
                formPanel.setVisible(false);
            }
        });
        formPanel.add(backButton);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setBounds(300, 400, 200, 50);
        formPanel.add(statusLabel);

        add(formPanel);
        formPanel.setVisible(false);

        setVisible(true);
    }

    private void showLoginForm(boolean isFuncionario) {
        mainPanel.setVisible(false);
        formPanel.setVisible(true);

        loginClienteButton.setVisible(!isFuncionario);
        cadastrarClienteButton.setVisible(!isFuncionario); // Show the register button for clients
        loginFuncionarioButton.setVisible(isFuncionario);
    }

    private void login(boolean isFuncionario) {
        String documento = documentoField.getText();
        String senha = new String(senhaField.getPassword());

        try {
            boolean success;
            if (isFuncionario) {
                success = controler.loginFuncionario(documento, senha);
                if (success) {
                    String nome = controler.getNomeTitular(documento);
                    String cargo = controler.getCargoFuncionario(documento);
                    showMenuFuncionario(nome, documento, cargo);
                }
            } else {
                success = controler.login(documento, senha);
                if (success) {
                    String nome = controler.getNomeTitular(documento);
                    String tipoConta = controler.getTipoConta(documento);
                    String numeroConta = controler.getNumeroConta(documento);
                    double saldo = controler.getSaldo(documento);
                    double divida = 0;
                    String dataVencimento = "";
                    if ("CORRENTE".equals(tipoConta)) {
                        divida = controler.getDivida();
                        dataVencimento = controler.getDataVencimento();
                    }
                    showMenuCliente(nome, tipoConta, numeroConta, saldo, divida, dataVencimento);
                }
            }

            if (!success) {
                showStatusMessage("Login falhou. Verifique suas credenciais.", Color.RED);
            }
        } catch (SQLException e) {
            showStatusMessage("Erro ao conectar ao banco de dados.", Color.RED);
        }
    }

    private void showMenuFuncionario(String nome, String codigoFuncionario, String cargo) throws SQLException {
        new FuncionarioMenu(controler, nome, codigoFuncionario, cargo);
        dispose();
    }

    private void showMenuCliente(String nome, String tipoConta, String numeroConta, double saldo, double divida, String dataVencimento) {
        new ClienteMenu(controler, nome, tipoConta, numeroConta, saldo, divida, dataVencimento);
        dispose();
    }

    private void showStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}