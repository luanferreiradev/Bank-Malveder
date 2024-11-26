package controler;

import config.DatabaseConnection;
import model.Conta;
import model.Relatorio;
import model.dao.*;
import model.service.ExtratoVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import view.Login;

import javax.mail.MessagingException;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Controler {
    private static int idContaTemp;
    private static int idUsuarioTemp;
    private static int idClienteTemp;
    private static String documentoTemp;
    private static String nomeFuncionarioTemp;
    private static String cargoFuncionarioTemp;
    private static String codigoFuncionarioTemp;
    private static FuncionarioDAO funcionarioDAO;

    static {
        try {
            funcionarioDAO = new FuncionarioDAO(DatabaseConnection.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static int getIdContaTemp() {
        return idContaTemp;
    }

    public static void setIdContaTemp(int idContaTemp) {
        Controler.idContaTemp = idContaTemp;
    }

    public static int getIdUsuarioTemp() {
        return idUsuarioTemp;
    }

    public static void setIdUsuarioTemp(int idUsuarioTemp) {
        Controler.idUsuarioTemp = idUsuarioTemp;
    }


    public static int getIdClienteTemp() {
        return idClienteTemp;
    }

    public static void setIdClienteTemp(int idClienteTemp) {
        Controler.idClienteTemp = idClienteTemp;
    }

    public static String getDocumentoTemp() {
        return documentoTemp;
    }

    public static void setDocumentoTemp(String documentoTemp) {
        Controler.documentoTemp = documentoTemp;
    }

    public static void setNomeFuncionario(String nomeFuncionario) {
        Controler.nomeFuncionarioTemp = nomeFuncionario;
    }

    public static String getCargoFuncionario(String codigoFuncionario) {
        return cargoFuncionarioTemp;
    }

    public static void setCargoFuncionario(String cargoFuncionario) {
        Controler.cargoFuncionarioTemp = cargoFuncionario;
    }

    public static String getCodigoFuncionario() {
        return codigoFuncionarioTemp;
    }

    public static void setCodigoFuncionario(String codigoFuncionario) {
        Controler.codigoFuncionarioTemp = codigoFuncionario;
    }

    //-------------CLIENTE----------------

    public boolean login(String documento, String senha) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);

            if (!usuarioDAO.verificarDocumentoCadastrado(documento)) {
                System.out.println("Documento não encontrado.");
                return false;
            }

            int idUsuario = usuarioDAO.obterIdUsuarioPorDocumento(documento);
            if (!usuarioDAO.verificarSenha(idUsuario, senha)) {
                System.out.println("Senha incorreta.");
                return false;
            }

            int idCliente = funcionarioDAO.obterIdClientePorUsuario(idUsuario);
            int idConta = funcionarioDAO.obterIdContaPorCliente(idCliente);

            idContaTemp = idConta;
            idUsuarioTemp = idUsuario;
            idClienteTemp = idCliente;
            documentoTemp = documento;

            System.out.println("Login realizado com sucesso!");
            return true;
        }
    }

    public String getNomeTitular(String documento) throws SQLException {
        // Capturar o documento da pessoa logada
        documento = documentoTemp;

        UsuarioDAO usuarioDAO = new UsuarioDAO(DatabaseConnection.getConnection());

        // Chamar o método getNomeTitular do UsuarioDAO passando o documento capturado
        String nomeTitular = usuarioDAO.getNomeTitular(documento);

        return nomeTitular;
    }

    public String getTipoConta(String documento) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO(DatabaseConnection.getConnection());
        ContaDAO contaDAO = new ContaDAO(DatabaseConnection.getConnection());
        int idUsuario = usuarioDAO.obterIdUsuarioPorDocumento(documento);
        int idCliente = usuarioDAO.obterIdClientePorUsuario(idUsuario);

        int idConta = contaDAO.obterIdContaPorCliente(idCliente);

        // Obter o tipo da conta baseado no ID da conta
        String tipoConta = contaDAO.obterTipoContaPorId(idConta);

        return tipoConta;
    }

    public String getNumeroConta(String documento) throws SQLException {
        // Implement method to retrieve account number
        ContaDAO contaDAO = new ContaDAO(DatabaseConnection.getConnection());
        String numeroConta = contaDAO.obterNumeroContaPorDocumento(documento);
        return numeroConta;
    }

    public double getSaldo(String documento) throws SQLException {
        // Implement method to retrieve account balance
        ContaDAO contaDAO = new ContaDAO(DatabaseConnection.getConnection());
        double saldo = contaDAO.obterSaldoPorDocumento(documento);
        return saldo;
    }

    public Double getDivida() throws SQLException {
        // Implement method to retrieve account debt
        ContaDAO contaDAO = new ContaDAO(DatabaseConnection.getConnection());
        double divida = contaDAO.getDivida(idContaTemp);
        return divida;
    }

    public String getDataVencimento() throws SQLException {
        // Implement method to retrieve due date
        ContaDAO contaDAO = new ContaDAO(DatabaseConnection.getConnection());
        String dataVencimento = contaDAO.getDataVencimento(idContaTemp);
        return dataVencimento;
    }

    public double getLimite() throws SQLException {
        // Implement method to retrieve account limit
        ContaDAO contaDAO = new ContaDAO(DatabaseConnection.getConnection());
        double limite = contaDAO.getLimite(idContaTemp);
        return limite;
    }

    public void criarSolicitacao(SolicitacaoDAO solicitacaoDAO, UsuarioDAO usuarioDAO, ContaDAO contaDAO,
                                 String nome, String documento, String dataNascimentoString, String telefone,
                                 String senha, boolean isPessoaFisica, String endereco, int numeroCasa,
                                 String bairro, String cidade, String estado, int tipoContaOpcao) throws Exception {
        if (usuarioDAO.verificarDocumentoCadastrado(documento)) {
            System.out.println("Já existe um usuário com este documento.");
            return;
        }

        // Convert date from dd/MM/yyyy to yyyy-MM-dd
        String dataNascimentoSQL;
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = originalFormat.parse(dataNascimentoString);
            dataNascimentoSQL = targetFormat.format(date);
        } catch (ParseException e) {
            throw new SQLException("Erro ao converter a data de nascimento.", e);
        }

        String tipoUsuario = "CLIENTE"; // Definindo o tipo de usuário como "CLIENTE" por padrão
        Double saldo = 0.0; // Definindo o saldo como 0 por padrão

        String tipoConta;
        if (tipoContaOpcao == 1) {
            tipoConta = "CORRENTE";
        } else if (tipoContaOpcao == 2) {
            tipoConta = "POUPANCA";
        } else {
            System.out.println("Opção inválida!");
            return;
        }

        Conta conta = new Conta();
        String numeroConta = conta.criarNumeroConta(contaDAO);
        String agencia = "777"; // Definindo a agência como "777" por padrão

        solicitacaoDAO.criarSolicitacao(nome, documento, java.sql.Date.valueOf(dataNascimentoSQL), telefone, tipoUsuario, senha,
                isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, numeroConta, agencia, saldo, tipoConta);
        System.out.println("Solicitação criada com sucesso!");
    }

    private String convertDateToSQLFormat(String date) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date originalDate = originalFormat.parse(date);
        return targetFormat.format(originalDate);
    }

    public void fazerDeposito(ContaDAO contaDAO, double valor) throws Exception {
        if (!contaDAO.verificarDocumentoCadastrado(documentoTemp)) {
            System.out.println("Documento não encontrado.");
            return;
        }

        int idConta = contaDAO.obterIdContaPorDocumento(documentoTemp);
        contaDAO.depositar(idConta, valor);
        System.out.println("Depósito realizado com sucesso!");
    }

    public void fazerSaque(ContaDAO contaDAO, double valor) throws Exception {
        if (!contaDAO.verificarDocumentoCadastrado(documentoTemp)) {
            System.out.println("Documento não encontrado.");
            return;
        }

        int idConta = contaDAO.obterIdContaPorDocumento(documentoTemp);
        contaDAO.sacar(idConta, valor);
        System.out.println("Saque realizado com sucesso!");
        contaDAO.zerarSaldo(idConta);
    }

    public void fazerTransferencia(ContaDAO contaDAO, String documentoDestino, double valor) throws Exception {
        if (!contaDAO.verificarDocumentoCadastrado(documentoTemp)) {
            System.out.println("Documento de origem não encontrado.");
            return;
        }

        int idContaOrigem = contaDAO.obterIdContaPorDocumento(documentoTemp);
        if (!contaDAO.verificarDocumentoCadastrado(documentoDestino)) {
            System.out.println("Documento de destino não encontrado.");
            return;
        }

        int idContaDestino = contaDAO.obterIdContaPorDocumento(documentoDestino);
        contaDAO.transferir(idContaOrigem, idContaDestino, valor);
        System.out.println("Transferência realizada com sucesso!");
    }

    public void pagarDivida(ContaDAO contaDAO, double valor) throws Exception {
        if (!contaDAO.verificarDocumentoCadastrado(documentoTemp)) {
            System.out.println("Documento não encontrado.");
            return;
        }

        int idConta = contaDAO.obterIdContaPorDocumento(documentoTemp);
        contaDAO.pagarDivida(idConta, valor);
        System.out.println("Dívida paga com sucesso!");
    }

    public String consultarSaldo(ContaDAO contaDAO) throws Exception {
        if (!contaDAO.verificarDocumentoCadastrado(documentoTemp)) {
            return "Documento não encontrado.";
        }

        int idConta = contaDAO.obterIdContaPorDocumento(documentoTemp);
        return contaDAO.verSaldo(idConta);
    }

    public String consultarExtrato(ContaDAO contaDAO) throws SQLException {
        List<ExtratoVO> extrato = contaDAO.consultarExtrato(idContaTemp);

        if (extrato.isEmpty()) {
            return "Nenhuma transação encontrada para a conta.";
        } else {
            StringBuilder extratoInfo = new StringBuilder();
            ExtratoVO primeiroItem = extrato.get(0);
            extratoInfo.append("Nome do Dono da Conta: ").append(primeiroItem.getNome()).append("\n");
            extratoInfo.append("Documento: ").append(primeiroItem.getDocumento()).append("\n");
            extratoInfo.append("Número da Conta: ").append(primeiroItem.getNumeroConta()).append("\n");
            extratoInfo.append("Transações:\n");

            for (ExtratoVO extratoVO : extrato) {
                extratoInfo.append("--------------------------------------------------\n");
                extratoInfo.append("Tipo de Transação: ").append(extratoVO.getTransacao().getTipoTransacao()).append("\n");
                extratoInfo.append("Valor: ").append(extratoVO.getTransacao().getValor()).append("\n");
                extratoInfo.append("Data/Hora: ").append(extratoVO.getTransacao().getDataHora()).append("\n");
                extratoInfo.append("Status: ").append(extratoVO.getTransacao().getStatus()).append("\n");
                extratoInfo.append("--------------------------------------------------\n");
            }
            return extratoInfo.toString();
        }
    }


    //-------------FUNCIONÁRIO----------------

    public static void logout() {
        idContaTemp = 0;
        idUsuarioTemp = 0;
        idClienteTemp = 0;
        documentoTemp = null;

        System.out.println("Logout realizado com sucesso!");
        System.exit(0);
    }

    public boolean loginFuncionario(String codigoFuncionario, String senha) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            ContaDAO contaDAO = new ContaDAO(connection);

            if (!funcionarioDAO.verificarCodigoCadastrado(codigoFuncionario)) {
                System.out.println("Código do funcionário não encontrado.");
                return false;
            }

            idUsuarioTemp = funcionarioDAO.obterIdUsuarioPorCodigoFuncionario(codigoFuncionario);
            int idFuncionario = funcionarioDAO.obterIdFuncionarioPorCodigo(codigoFuncionario);
            int idUsuario = funcionarioDAO.obterIdUsuarioPorFuncionario(idFuncionario);

            if (!usuarioDAO.verificarSenha(idUsuario, senha)) {
                System.out.println("Senha incorreta.");
                return false;
            }

            idClienteTemp = usuarioDAO.obterIdClientePorUsuario(idUsuarioTemp);
            idContaTemp = contaDAO.obterIdContaPorCliente(idClienteTemp);
            nomeFuncionarioTemp = funcionarioDAO.obterNomeFuncionario(String.valueOf(codigoFuncionario));
            cargoFuncionarioTemp = funcionarioDAO.getCargoFuncionario(codigoFuncionario);

            System.out.println("Login realizado com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw e;
        }
    }

    public void excluirSolicitacao(int idSolicitacao) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            solicitacaoDAO.excluirSolicitacao(idSolicitacao);
            System.out.println("Solicitação excluída com sucesso!");
        }
    }

    public void listarSolicitacoes() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            ResultSet resultSet = solicitacaoDAO.listarSolicitacoes();

            StringBuilder solicitacoes = new StringBuilder();
            solicitacoes.append("<html><table border='1'><tr>")
                    .append("<th>ID</th><th>Documento</th><th>Senha</th><th>Pessoa Física</th><th>Endereço</th><th>Número</th><th>Bairro</th><th>Cidade</th><th>Estado</th><th>Data Nascimento</th><th>Nome</th><th>Telefone</th><th>Tipo Conta</th><th>ID Status</th><th>Status</th>")
                    .append("</tr>");

            while (resultSet.next()) {
                int idSolicitacao = resultSet.getInt("id_solicitacao");
                String documento = resultSet.getString("documento");
                String senha = resultSet.getString("senha");
                boolean isPessoaFisica = resultSet.getBoolean("is_pessoa_fisica");
                String endereco = resultSet.getString("endereco");
                String numeroCasa = resultSet.getString("numero_casa");
                String bairro = resultSet.getString("bairro");
                String cidade = resultSet.getString("cidade");
                String estado = resultSet.getString("estado");
                Date dataNascimento = resultSet.getDate("data_nascimento");
                String nome = resultSet.getString("nome");
                String telefone = resultSet.getString("telefone");
                String tipoConta = resultSet.getString("tipo_conta");
                int idStatus = resultSet.getInt("id_status");
                String status = resultSet.getString("status");

                solicitacoes.append("<tr>")
                        .append("<td>").append(idSolicitacao).append("</td>")
                        .append("<td>").append(documento).append("</td>")
                        .append("<td>").append(senha).append("</td>")
                        .append("<td>").append(isPessoaFisica).append("</td>")
                        .append("<td>").append(endereco).append("</td>")
                        .append("<td>").append(numeroCasa).append("</td>")
                        .append("<td>").append(bairro).append("</td>")
                        .append("<td>").append(cidade).append("</td>")
                        .append("<td>").append(estado).append("</td>")
                        .append("<td>").append(dataNascimento).append("</td>")
                        .append("<td>").append(nome).append("</td>")
                        .append("<td>").append(telefone).append("</td>")
                        .append("<td>").append(tipoConta).append("</td>")
                        .append("<td>").append(idStatus).append("</td>")
                        .append("<td>").append(status).append("</td>")
                        .append("</tr>");
            }

            solicitacoes.append("</table></html>");

            JOptionPane.showMessageDialog(null, solicitacoes.toString(), "Lista de Solicitações", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public void aprovarSolicitacao(SolicitacaoDAO solicitacaoDAO, UsuarioDAO usuarioDAO, ContaDAO contaDAO, int idSolicitacao) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            ResultSet rs = solicitacaoDAO.listarSolicitacoes();
            boolean found = false;
            while (rs.next()) {
                if (rs.getInt("id_solicitacao") == idSolicitacao) {
                    found = true;
                    String nome = rs.getString("nome");
                    String documento = rs.getString("documento");
                    java.sql.Date dataNascimento = rs.getDate("data_nascimento");
                    String telefone = rs.getString("telefone");
                    String tipoUsuario = "CLIENTE";
                    String senha = rs.getString("senha");
                    boolean isPessoaFisica = rs.getBoolean("is_pessoa_fisica");
                    String endereco = rs.getString("endereco");
                    int numeroCasa = rs.getInt("numero_casa");
                    String bairro = rs.getString("bairro");
                    String cidade = rs.getString("cidade");
                    String estado = rs.getString("estado");
                    String numeroConta = rs.getString("numero_conta");
                    String agencia = rs.getString("agencia");
                    double saldo = rs.getDouble("saldo");
                    String tipoConta = rs.getString("tipo_conta");
                    String status = "APROVADO";

                    if (contaDAO.verificarDivida(rs.getInt("id_solicitacao"))) {
                        throw new SQLException("Não é possível aprovar a solicitação com dívidas pendentes.");
                    }

                    solicitacaoDAO.aprovarSolicitacao(idSolicitacao);
                    usuarioDAO.criarUsuario(nome, documento, dataNascimento, telefone, tipoUsuario, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, numeroConta, agencia, saldo, tipoConta, status);
                    System.out.println("Solicitação aprovada e usuário criado com sucesso!");
                    break;
                }
            }
            if (!found) {
                System.out.println("Solicitação não encontrada!");
            }
        }
    }


    public void removerUsuario(String documentoUsuario) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            if (!usuarioDAO.verificarDocumentoCadastrado(documentoUsuario)) {
                System.out.println("Documento não encontrado.");
                return;
            }
            int idUsuario = usuarioDAO.obterIdUsuarioPorDocumento(documentoUsuario);
            usuarioDAO.deletarUsuario(idUsuario);
            System.out.println("Usuário removido com sucesso!");
        }
    }

    public static void atualizarCliente(Integer idCliente, String nome, String documento, String dataNascimentoStr, String telefone,
                                        String endereco, Integer numeroCasa, String bairro, String cidade, String estado, String tipoConta) throws SQLException {
        // Obter os dados atuais do cliente
        ResultSet rs = funcionarioDAO.lerClienteCompleto(idCliente);
        if (!rs.next()) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        // Se algum campo vier null, usar o valor atual do cliente
        if (nome == null) {
            nome = rs.getString("nome");
        }
        if (documento == null) {
            documento = rs.getString("documento");
        }
        if (dataNascimentoStr == null) {
            dataNascimentoStr = rs.getDate("data_nascimento").toString();
        } else {
            // Convert date from dd/MM/yyyy to yyyy-MM-dd
            try {
                SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = originalFormat.parse(dataNascimentoStr);
                dataNascimentoStr = targetFormat.format(date);
            } catch (ParseException e) {
                throw new SQLException("Erro ao converter a data de nascimento.", e);
            }
        }
        if (telefone == null) {
            telefone = rs.getString("telefone");
        }
        if (endereco == null) {
            endereco = rs.getString("endereco");
        }
        if (numeroCasa == null) {
            numeroCasa = rs.getInt("numero_casa");
        }
        if (bairro == null) {
            bairro = rs.getString("bairro");
        }
        if (cidade == null) {
            cidade = rs.getString("cidade");
        }
        if (estado == null) {
            estado = rs.getString("estado");
        }
        if (tipoConta == null) {
            tipoConta = rs.getString("tipo_conta");
        }

        java.sql.Date dataNascimento = java.sql.Date.valueOf(dataNascimentoStr);
        funcionarioDAO.atualizarCliente(idCliente, nome, documento, dataNascimento, telefone, endereco, numeroCasa, bairro, cidade, estado, tipoConta);
        System.out.println("Cliente atualizado com sucesso.");
    }

    // src/controler/Controler.java
    public void atualizarConta(Integer idUsuario, Boolean alterarDadosConta, Boolean alterarTipoConta, Double novoLimite, String novaDataVencimentoStr, Double novaTaxaRendimento) throws SQLException {
        // Obter o ID da conta associada ao usuário
        int idCliente = funcionarioDAO.obterIdClientePorUsuario(idUsuario);
        int idConta = funcionarioDAO.obterIdContaPorCliente(idCliente);

        // Obter os dados atuais da conta
        ResultSet rs = funcionarioDAO.lerConta(idConta);
        if (!rs.next()) {
            System.out.println("Conta não encontrada.");
            return;
        }

        String numeroConta = rs.getString("numero_conta");
        String agencia = rs.getString("agencia");
        double saldo = rs.getDouble("saldo");
        String tipoConta = rs.getString("tipo_conta");
        double limiteAtual = rs.getDouble("limite");
        java.sql.Date dataVencimentoAtual = rs.getDate("data_vencimento");
        double taxaRendimentoAtual = rs.getDouble("taxa_rendimento");

        System.out.println("Número da Conta: " + numeroConta);
        System.out.println("Agência: " + agencia);
        System.out.println("Saldo: " + saldo);
        System.out.println("Tipo de Conta Atual: " + tipoConta);

        // Se algum campo vier null, usar o valor atual da conta
        if (novoLimite == null) {
            novoLimite = limiteAtual;
        }
        if (novaDataVencimentoStr == null) {
            novaDataVencimentoStr = dataVencimentoAtual.toString();
        } else {
            // Convert date from dd/MM/yyyy to yyyy-MM-dd
            try {
                SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = originalFormat.parse(novaDataVencimentoStr);
                novaDataVencimentoStr = targetFormat.format(date);
            } catch (ParseException e) {
                throw new SQLException("Erro ao converter a data de vencimento.", e);
            }
        }
        if (novaTaxaRendimento == null) {
            novaTaxaRendimento = taxaRendimentoAtual;
        }

        String novoTipoConta = tipoConta.equals("CORRENTE") ? "POUPANCA" : (tipoConta.equals("POUPANCA") ? "CORRENTE" : tipoConta);

        if (novoTipoConta.equals(tipoConta)) {
            System.out.println("O tipo de conta é o mesmo. Nenhuma alteração será feita.");
            return;
        }

        if (alterarDadosConta) {
            updateConta(tipoConta, idUsuario, novoLimite, novaDataVencimentoStr, novaTaxaRendimento);
        }

        if (alterarTipoConta) {
            if (novoTipoConta.equals("CORRENTE")) {
                // Se estamos mudando para corrente, precisamos de novo limite e data de vencimento
                java.sql.Date novaDataVencimento = java.sql.Date.valueOf(novaDataVencimentoStr);
                String resultado = funcionarioDAO.alterarTipoConta(idCliente, "CORRENTE", novoLimite, novaDataVencimento);
                System.out.println("Tipo de conta alterado. Status: " + resultado);
            } else if (novoTipoConta.equals("POUPANCA")) {
                // Se estamos mudando para poupança, obter a taxa de rendimento
                String resultado = funcionarioDAO.alterarTipoConta(idCliente, "POUPANCA", 0, null);
                System.out.println("Tipo de conta alterado. Status: " + resultado);
                double taxaRendimento = funcionarioDAO.obterTaxaRendimento(idCliente);
                System.out.println("Taxa de Rendimentos da Conta Poupança: " + taxaRendimento);
            }

            System.out.println("Conta atualizada com sucesso.");
        } else {
            System.out.println("Nenhuma alteração foi feita.");
        }
    }
    private void updateConta(String tipoConta, int idUsuario, double novoLimite, String novaDataVencimentoStr, double novaTaxaRendimento) throws SQLException {
        if (tipoConta.equals("CORRENTE")) {
            java.sql.Date novaDataVencimento = java.sql.Date.valueOf(novaDataVencimentoStr);
            funcionarioDAO.updateConta(idUsuario, novoLimite, novaDataVencimento, 0.0, tipoConta);
        } else if (tipoConta.equals("POUPANCA")) {
            funcionarioDAO.updateConta(idUsuario, 0.0, null, novaTaxaRendimento, tipoConta);
        }
        System.out.println("Conta " + tipoConta + " atualizada com sucesso!");
    }

    public ResultSet lerCliente(int idCliente) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
            ResultSet rs = funcionarioDAO.lerClienteCompleto(idCliente);
            FuncionarioDAO funcionarioDAO1 = new FuncionarioDAO(connection);
            return funcionarioDAO1.lerClienteCompleto(idCliente);
        }
    }

    public ResultSet lerConta(int idCliente) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
            return funcionarioDAO.lerConta(idCliente);
        }
    }

    public void verificarECriarFuncionarioRoot() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
            ResultSet resultSet = funcionarioDAO.verificarFuncionario("0001", "root");

            if (!resultSet.next()) {
                // Funcionario root does not exist, create it
                criarFuncionario("root", "00000000000", "01/01/1970", "0000000000", "Administrador", "root", "00000-000", "Local", 0, "Bairro", "Cidade", "ES", 0.0);
                System.out.println("Funcionario root criado com sucesso.");
            } else {
                System.out.println("Funcionario root já existe.");
            }
        }
    }

    public static void criarFuncionario(String nome, String documento, String dataNascimentoStr, String telefone, String cargo, String senha, String cep, String local, int numeroCasa, String bairro, String cidade, String estado, double salario) throws SQLException {
        try {
            // Convert date from dd/MM/yyyy to yyyy-MM-dd
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = originalFormat.parse(dataNascimentoStr);
            String formattedDate = targetFormat.format(date);
            java.sql.Date dataNascimento = java.sql.Date.valueOf(formattedDate);

            String codigoFuncionario = funcionarioDAO.gerarCodigoFuncionario();
            Conta conta = new Conta();
            String numeroContaPoupanca = conta.criarNumeroConta(new ContaDAO(DatabaseConnection.getConnection()));
            funcionarioDAO.criarFuncionario(nome, documento, dataNascimento, telefone, cargo, codigoFuncionario, senha, cep, local, numeroCasa, bairro, cidade, estado.substring(0, 2), salario, numeroContaPoupanca);
            System.out.println("Funcionário criado com sucesso. Número da conta poupança: " + numeroContaPoupanca);
        } catch (ParseException e) {
            throw new SQLException("Erro ao converter a data de nascimento.", e);
        }
    }
    public static void deletarFuncionario(int idFuncionario) throws SQLException {
        funcionarioDAO.deletarFuncionario(idFuncionario);
        JOptionPane.showMessageDialog(null, "Funcionário deletado com sucesso.", "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    public void lerFuncionario(int idFuncionario) throws SQLException, IOException, ParseException {
        ResultSet resultSet = funcionarioDAO.lerFuncionario(idFuncionario);
        if (resultSet.next()) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

            String dataNascimentoFuncionario = targetFormat.format(originalFormat.parse(resultSet.getString("data_nascimento_funcionario")));
            String dataNascimentoUsuario = targetFormat.format(originalFormat.parse(resultSet.getString("data_nascimento_usuario")));

            StringBuilder funcionarioInfo = new StringBuilder();
            funcionarioInfo.append("ID: ").append(resultSet.getInt("id_funcionario")).append("\n");
            funcionarioInfo.append("Código do Funcionário: ").append(resultSet.getString("codigo_funcionario")).append("\n");
            funcionarioInfo.append("Cargo: ").append(resultSet.getString("cargo")).append("\n");
            funcionarioInfo.append("Data de Nascimento (Funcionário): ").append(dataNascimentoFuncionario).append("\n");
            funcionarioInfo.append("Nome: ").append(resultSet.getString("nome")).append("\n");
            funcionarioInfo.append("Documento: ").append(resultSet.getString("documento")).append("\n");
            funcionarioInfo.append("Data de Nascimento (Usuário): ").append(dataNascimentoUsuario).append("\n");
            funcionarioInfo.append("Telefone: ").append(resultSet.getString("telefone")).append("\n");
            funcionarioInfo.append("Senha: ").append(resultSet.getString("senha")).append("\n");
            funcionarioInfo.append("CEP: ").append(resultSet.getString("cep")).append("\n");
            funcionarioInfo.append("Local: ").append(resultSet.getString("local")).append("\n");
            funcionarioInfo.append("Número da Casa: ").append(resultSet.getInt("numero_casa")).append("\n");
            funcionarioInfo.append("Bairro: ").append(resultSet.getString("bairro")).append("\n");
            funcionarioInfo.append("Cidade: ").append(resultSet.getString("cidade")).append("\n");
            funcionarioInfo.append("Estado: ").append(resultSet.getString("estado")).append("\n");
            funcionarioInfo.append("ID do Cliente: ").append(resultSet.getInt("id_cliente")).append("\n");

            // Display in JOptionPane
            JOptionPane.showMessageDialog(null, funcionarioInfo.toString(), "Informações do Funcionário", JOptionPane.INFORMATION_MESSAGE);

            // Export to Excel
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Funcionário");

            String[] lines = funcionarioInfo.toString().split("\n");
            int rowNum = 0;
            for (String line : lines) {
                Row row = sheet.createRow(rowNum++);
                String[] columns = line.split(": ");
                for (int colNum = 0; colNum < columns.length; colNum++) {
                    Cell cell = row.createCell(colNum);
                    cell.setCellValue(columns[colNum]);
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream("funcionario.xls")) {
                workbook.write(fileOut);
            }
            workbook.close();
            System.out.println("Funcionário exportado para Excel com sucesso.");
        } else {
            JOptionPane.showMessageDialog(null, "Funcionário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarFuncionario(int idFuncionario, String nome, String documento, String dataNascimentoStr, String telefone, String cargo, String cep, String local, int numeroCasa, String bairro, String cidade, String estado, String senha) throws SQLException, ParseException {
        ResultSet resultSet = funcionarioDAO.lerFuncionario(idFuncionario);
        if (resultSet.next()) {
            if (nome == null) {
                nome = resultSet.getString("nome");
            }
            if (documento == null) {
                documento = resultSet.getString("documento");
            }
            if (dataNascimentoStr == null) {
                dataNascimentoStr = resultSet.getDate("data_nascimento").toString();
            } else {
                // Convert date from dd/MM/yyyy to yyyy-MM-dd
                SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = originalFormat.parse(dataNascimentoStr);
                dataNascimentoStr = targetFormat.format(date);
            }
            if (telefone == null) {
                telefone = resultSet.getString("telefone");
            }
            if (cargo == null) {
                cargo = resultSet.getString("cargo");
            }
            if (cep == null) {
                cep = resultSet.getString("cep");
            }
            if (local == null) {
                local = resultSet.getString("local");
            }
            if (numeroCasa == 0) {
                numeroCasa = resultSet.getInt("numero_casa");
            }
            if (bairro == null) {
                bairro = resultSet.getString("bairro");
            }
            if (cidade == null) {
                cidade = resultSet.getString("cidade");
            }
            if (estado == null) {
                estado = resultSet.getString("estado");
            }
            if (senha == null) {
                senha = resultSet.getString("senha");
            }
            String codigoFuncionario = resultSet.getString("codigo_funcionario");

            java.sql.Date dataNascimento = java.sql.Date.valueOf(dataNascimentoStr);
            funcionarioDAO.atualizarFuncionario(idFuncionario, nome, documento, dataNascimento, telefone, cargo, codigoFuncionario, senha, cep, local, numeroCasa, bairro, cidade, estado);
            System.out.println("Funcionário atualizado com sucesso.");
        } else {
            throw new SQLException("Funcionário não encontrado.");
        }
    }

    public void gerarRelatorioMovimentacao(RelatorioDAO relatorioDAO, int tipoRelatorio, int idConta, int idUsuario, boolean exportar, String caminhoDiretorio, int idFuncionario) throws SQLException, IOException, MessagingException, GeneralSecurityException {
        String conteudo = "";
        String tipoRelatorioStr = "";

        if (tipoRelatorio == 1) {
            conteudo = relatorioDAO.gerarRelatorioMovimentacao(idConta);
            tipoRelatorioStr = "Relatório de Movimentação";
        } else if (tipoRelatorio == 2) {
            conteudo = relatorioDAO.gerarRelatorioInformacoes(idUsuario);
            tipoRelatorioStr = "Relatório de Informações";
        } else if (tipoRelatorio == 3) {
            conteudo = relatorioDAO.gerarRelatorioGeral(idConta, idUsuario);
            tipoRelatorioStr = "Relatório Geral";
        }

        Relatorio relatorio = new Relatorio(tipoRelatorioStr, conteudo, idFuncionario);

        if (exportar) {
            relatorioDAO.exportarRelatorioParaExcel(relatorio.getConteudo(), caminhoDiretorio);
        }

        JOptionPane.showMessageDialog(null, relatorio.getConteudo(), relatorio.getTipoRelatorio(), JOptionPane.INFORMATION_MESSAGE);
    }
}


