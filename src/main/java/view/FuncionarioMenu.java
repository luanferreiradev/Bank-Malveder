// src/view/FuncionarioMenu.java
package view;

import controler.Controler;
import config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FuncionarioMenu extends JFrame {
    private Controler controler;
    private JLabel backButton;
    private JLabel titleLabel;
    private JLabel imageLabel;
    private JLabel infoLabel;
    private JLabel timeLabel;
    private JButton solicitacoesButton;
    private JButton aprovaSolicitacaoButton;
    private JButton removerUsuarioButton;
    private JButton atualizarClienteButton;
    private JButton atualizarContaButton;
    private JButton dadosClienteButton;
    private JButton dadosContaButton;
    private JButton gerarRelatorioButton;
    private Connection connection;
    private String nome;
    private String codigoFuncionario;
    private String cargo;
    private FuncionarioDAO funcionarioDAO;

    public FuncionarioMenu(Controler controler, String nome, String codigoFuncionario, String cargo) {
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

        setTitle("Funcionario Menu");
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

        // Buttons
        solicitacoesButton = new JButton("Solicitações");
        solicitacoesButton.setBounds(50, 310, 150, 50);
        solicitacoesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controler.listarSolicitacoes();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao listar solicitações: " + ex.getMessage());
                }
            }
        });
        add(solicitacoesButton);

        aprovaSolicitacaoButton = new JButton("Aprovar Solicitação");
        aprovaSolicitacaoButton.setBounds(250, 310, 150, 50);
        aprovaSolicitacaoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idSolicitacaoStr = JOptionPane.showInputDialog("Digite o ID da solicitação:");
                try {
                    int idSolicitacao = Integer.parseInt(idSolicitacaoStr);
                    controler.aprovarSolicitacao(new SolicitacaoDAO(connection), new UsuarioDAO(connection), new ContaDAO(connection), idSolicitacao);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao aprovar solicitação: " + ex.getMessage());
                }
            }
        });
        add(aprovaSolicitacaoButton);

        removerUsuarioButton = new JButton("Remover Usuário");
        removerUsuarioButton.setBounds(450, 310, 150, 50);
        removerUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String documentoUsuario = JOptionPane.showInputDialog("Digite o documento do usuário:");
                try {
                    controler.removerUsuario(documentoUsuario);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao remover usuário: " + ex.getMessage());
                }
            }
        });
        add(removerUsuarioButton);

        atualizarClienteButton = new JButton("Atualizar Cliente");
        atualizarClienteButton.setBounds(50, 400, 150, 50);
        atualizarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AtualizarCliente(controler).setVisible(true);
            }
        });
        add(atualizarClienteButton);

        atualizarContaButton = new JButton("Atualizar Conta");
        atualizarContaButton.setBounds(250, 400, 150, 50);
        atualizarContaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AtualizarConta(controler).setVisible(true);
            }
        });
        add(atualizarContaButton);

        dadosClienteButton = new JButton("Dados Cliente");
        dadosClienteButton.setBounds(450, 400, 150, 50);
        dadosClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idClienteStr = JOptionPane.showInputDialog("Digite o ID do cliente:");
                try {
                    int idCliente = Integer.parseInt(idClienteStr);
                    mostrarDadosCliente(idCliente);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID do cliente inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(dadosClienteButton);

        dadosContaButton = new JButton("Dados Conta");
        dadosContaButton.setBounds(50, 500, 150, 50);
        dadosContaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idClienteStr = JOptionPane.showInputDialog("Digite o ID do cliente:");
                try {
                    int idCliente = Integer.parseInt(idClienteStr);
                    mostrarDadosConta(idCliente);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID do cliente inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(dadosContaButton);

        gerarRelatorioButton = new JButton("Gerar Relatório de Movimentações");
        gerarRelatorioButton.setBounds(250, 500, 250, 50);
        gerarRelatorioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int idConta = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID da conta:"));
                    int idUsuario = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do usuário:"));
                    int idFuncionario = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do funcionário:"));
                    int tipoRelatorio = Integer.parseInt(JOptionPane.showInputDialog("Digite o tipo de relatório (1: Movimentação, 2: Informações, 3: Geral):"));
                    int exportarOption = JOptionPane.showConfirmDialog(null, "Deseja exportar para Excel?", "Exportar", JOptionPane.YES_NO_OPTION);
                    boolean exportar = (exportarOption == JOptionPane.YES_OPTION);
                    String caminhoDiretorio = "";
                    if (exportar) {
                        caminhoDiretorio = JOptionPane.showInputDialog("Digite o caminho do diretório para salvar o arquivo:");
                    }
                    controler.gerarRelatorioMovimentacao(new RelatorioDAO(DatabaseConnection.getConnection()), tipoRelatorio, idConta, idUsuario, exportar, caminhoDiretorio, idFuncionario);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao gerar relatório: " + ex.getMessage());
                }
            }
        });
        add(gerarRelatorioButton);

        if ("root".equals(cargo)) {
            JButton rootMenuButton = new JButton("Root Menu");
            rootMenuButton.setBounds(520, 500, 250, 50);
            rootMenuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleRootMenuAction();
                }
            });
            add(rootMenuButton);
        }

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

    private void mostrarDadosCliente(int idCliente) {
        try {
            StringBuilder dados = new StringBuilder();
            Controler controler = new Controler();
            try (ResultSet rs = controler.lerCliente(idCliente)) {
                while (rs.next()) {
                    dados.append("ID: ").append(rs.getInt("id_cliente")).append("\n");
                    dados.append("Nome: ").append(rs.getString("nome")).append("\n");
                    dados.append("Documento: ").append(rs.getString("documento")).append("\n");
                    dados.append("Data de Nascimento: ").append(rs.getDate("data_nascimento")).append("\n");
                    dados.append("Telefone: ").append(rs.getString("telefone")).append("\n");
                    dados.append("Endereço: ").append(rs.getString("endereco")).append("\n");
                    dados.append("Número da Casa: ").append(rs.getInt("numero_casa")).append("\n");
                    dados.append("Bairro: ").append(rs.getString("bairro")).append("\n");
                    dados.append("Cidade: ").append(rs.getString("cidade")).append("\n");
                    dados.append("Estado: ").append(rs.getString("estado")).append("\n");
                    dados.append("Tipo de Conta: ").append(rs.getString("tipo_conta")).append("\n");
                    dados.append("Agência: ").append(rs.getString("agencia")).append("\n");
                    dados.append("Saldo: ").append(rs.getDouble("saldo")).append("\n");
                    if ("CORRENTE".equals(rs.getString("tipo_conta"))) {
                        dados.append("Limite: ").append(rs.getDouble("limite")).append("\n");
                        dados.append("Data de Vencimento: ").append(rs.getDate("data_vencimento")).append("\n");
                        dados.append("Dívida: ").append(rs.getDouble("divida")).append("\n");
                    } else if ("POUPANCA".equals(rs.getString("tipo_conta"))) {
                        dados.append("Taxa de Rendimento: ").append(rs.getDouble("taxa_rendimento")).append("\n");
                    }
                }
            }
            JOptionPane.showMessageDialog(this, dados.toString(), "Dados do Cliente", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao obter dados do cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDadosConta(int idCliente) {
        try {
            StringBuilder dados = new StringBuilder();
            Controler controler = new Controler();
            ResultSet rs = controler.lerConta(idCliente);
            while (rs.next()) {
                dados.append("ID da Conta: ").append(rs.getInt("id_conta")).append("\n");
                dados.append("Número da Conta: ").append(rs.getString("numero_conta")).append("\n");
                dados.append("Agência: ").append(rs.getString("agencia")).append("\n");
                dados.append("Saldo: ").append(rs.getDouble("saldo")).append("\n");
                dados.append("Tipo de Conta: ").append(rs.getString("tipo_conta")).append("\n");
                dados.append("Status: ").append(rs.getString("status")).append("\n");
                if (rs.getString("tipo_conta").equals("CORRENTE")) {
                    dados.append("Limite: ").append(rs.getDouble("limite")).append("\n");
                    dados.append("Data de Vencimento: ").append(rs.getDate("data_vencimento")).append("\n");
                    dados.append("Dívida: ").append(rs.getDouble("divida")).append("\n");
                } else if (rs.getString("tipo_conta").equals("POUPANCA")) {
                    dados.append("Taxa de Rendimento: ").append(rs.getDouble("taxa_rendimento")).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, dados.toString(), "Dados da Conta", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao obter dados da conta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRootMenuAction() {
        new RootMenu(controler, nome, codigoFuncionario, cargo).setVisible(true);
    }
}