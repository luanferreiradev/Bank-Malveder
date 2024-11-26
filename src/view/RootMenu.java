package view;

import controler.Controler;
import config.DatabaseConnection;
import model.dao.FuncionarioDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RootMenu extends JFrame {
    private Controler controler;
    private JLabel backButton;
    private JLabel titleLabel;
    private JLabel imageLabel;
    private JLabel infoLabel;
    private JLabel timeLabel;
    private Connection connection;
    private String nome;
    private String codigoFuncionario;
    private String cargo;
    private FuncionarioDAO funcionarioDAO;

    public RootMenu(Controler controler, String nome, String codigoFuncionario, String cargo) {
        this.controler = controler;
        this.nome = nome;
        this.codigoFuncionario = codigoFuncionario;
        this.cargo = cargo;

        // Initialize the database connection
        try {
            this.connection = DatabaseConnection.getConnection();
            this.funcionarioDAO = new FuncionarioDAO(connection);
            this.nome = funcionarioDAO.obterNomeFuncionario(codigoFuncionario);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        setTitle("Root Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        // Back button
        ImageIcon backIcon = new ImageIcon("/home/luanferreira/workspaces/ws-sts/BancoMalvaderNew/img/seta-esquerda.png");
        Image backImage = backIcon.getImage();
        Image scaledBackImage = backImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledBackIcon = new ImageIcon(scaledBackImage);
        backButton = new JLabel(scaledBackIcon);
        backButton.setBounds(10, 10, 50, 50);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new FuncionarioMenu(controler, nome, codigoFuncionario, cargo).setVisible(true);
                dispose();
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
        imageLabel.setBounds(600, 10, 150, 150);
        add(imageLabel);

        // Info label
        infoLabel = new JLabel();
        infoLabel.setBounds(50, 140, 400, 150);
        add(infoLabel);
        updateInfoLabel();

        // Time label
        timeLabel = new JLabel();
        timeLabel.setBounds(50, 250, 400, 50);
        add(timeLabel);
        updateTimeLabel();

        //Buttons
        JButton cadastrarFuncionarioButton = new JButton("Cadastrar Funcionário");
        cadastrarFuncionarioButton.setBounds(50, 310, 200, 50);
        cadastrarFuncionarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CadastroFuncionario(controler).setVisible(true);
            }
        });
        add(cadastrarFuncionarioButton);

        JButton deletarFuncionarioButton = new JButton("Deletar Funcionário");
        deletarFuncionarioButton.setBounds(300, 310, 200, 50);
        deletarFuncionarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idFuncionarioStr = JOptionPane.showInputDialog("Digite o ID do funcionário:");
                try {
                    int idFuncionario = Integer.parseInt(idFuncionarioStr);
                    controler.deletarFuncionario(idFuncionario);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao deletar funcionário: " + ex.getMessage());
                }
            }
        });
        add(deletarFuncionarioButton);

        JButton lerFuncionarioButton = new JButton("Ler Funcionário");
        lerFuncionarioButton.setBounds(50, 400, 200, 50);
        lerFuncionarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idFuncionarioStr = JOptionPane.showInputDialog("Digite o ID do funcionário:");
                try {
                    int idFuncionario = Integer.parseInt(idFuncionarioStr);
                    controler.lerFuncionario(idFuncionario);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao ler funcionário: " + ex.getMessage());
                }
            }
        });
        add(lerFuncionarioButton);

        JButton atualizarFuncionarioButton = new JButton("Atualizar Funcionário");
        atualizarFuncionarioButton.setBounds(300, 400, 200, 50);
        atualizarFuncionarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AtualizarFuncionario(controler).setVisible(true);
            }
        });
        add(atualizarFuncionarioButton);

        setVisible(true);
    }

    private void updateInfoLabel() {
        StringBuilder info = new StringBuilder();
        info.append("<html><div style='font-size:14px; line-height:1.5;'>");
        info.append("<b style='font-size:16px;'>Bem-vindo, ").append(nome).append("</b><br>");
        info.append("Código do Funcionário: ").append(codigoFuncionario).append("<br>");
        info.append("Cargo: ").append(cargo).append("<br>");
        info.append("</div></html>");
        infoLabel.setText(info.toString());
    }

    private void updateTimeLabel() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
                String time = sdf.format(new Date());
                timeLabel.setText("Hora Atual: " + time);
            }
        });
        timer.start();
    }
}