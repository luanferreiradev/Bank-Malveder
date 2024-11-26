package model;

import config.DatabaseConnection;
import controler.Controler;
import model.dao.*;
import model.service.ExtratoVO;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;


// Classe de Teste
public class TesteSolicitacao {
    private static FuncionarioDAO funcionarioDAO;
    private static ContaDAO contaDAO;
    private static RelatorioDAO relatorioDAO;
    private static int idContaTemp;
    private static int idUsuarioTemp;
    private static int idClienteTemp;
    private static String documentoTemp;

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            contaDAO = new ContaDAO(connection); // Inicializa contaDAO
            funcionarioDAO = new FuncionarioDAO(connection); // Inicializa funcionarioDAO
            relatorioDAO = new RelatorioDAO(connection); // Inicializa relatorioDAO
            Scanner scanner = new Scanner(System.in);
            int option;


            String nome7 = funcionarioDAO.obterNomeFuncionario(String.valueOf(21784));
            System.out.println(nome7);


            do {
                System.out.println("Menu:");
                System.out.println("1. Listar solicitações");
                System.out.println("2. Aprovar solicitação");
                System.out.println("3. Rejeitar solicitação");
                System.out.println("4. Criar solicitação");
                System.out.println("5. Fazer depósito");
                System.out.println("6. Fazer saque");
                System.out.println("7. Fazer transferência");
                System.out.println("8. Pagar dívida");
                System.out.println("9. Remover usuário");
                System.out.println("10. Excluir solicitação");
                System.out.println("11. Atualizar Cliente");
                System.out.println("12. Atualizar Conta");
                System.out.println("13. Ler Cliente");
                System.out.println("14. Ler Conta");
                System.out.println("15. Consultar Saldo");
                System.out.println("16. Criar Funcionario");
                System.out.println("17. Atualizar Funcionario");
                System.out.println("18. Ler Funcionario");
                System.out.println("19. Excluir Funcionario");
                System.out.println("20. Consultar Extrato");
                System.out.println("21. Gerar Relatório de Movimentação");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        listarSolicitacoes(solicitacaoDAO);
                        break;
                    case 2:
                        System.out.print("Digite o ID da solicitação a ser aprovada: ");
                        int idAprovar = scanner.nextInt();
                        aprovarSolicitacao(solicitacaoDAO, usuarioDAO, contaDAO, idAprovar);
                        break;
                    case 3:
                        System.out.print("Digite o ID da solicitação a ser rejeitada: ");
                        int idRejeitar = scanner.nextInt();
                        solicitacaoDAO.rejeitarSolicitacao(idRejeitar);
                        break;
                    case 4:
                        criarSolicitacao(solicitacaoDAO, usuarioDAO, contaDAO, scanner);
                        break;
                    case 5:
                        fazerDeposito(contaDAO, scanner);
                        break;
                    case 6:
                        fazerSaque(contaDAO, scanner);
                        break;
                    case 7:
                        fazerTransferencia(contaDAO, scanner);
                        break;
                    case 8:
                        pagarDivida(contaDAO, scanner);
                        break;
                    case 9:
                        removerUsuario(usuarioDAO, scanner);
                        break;
                    case 10:
                        System.out.print("Digite o ID da solicitação a ser excluída: ");
                        int idExcluir = scanner.nextInt();
                        excluirSolicitacao(solicitacaoDAO, idExcluir);
                        break;
                    case 11:
                        atualizarCliente(scanner);
                        break;
                    case 12:
                        atualizarConta(scanner);
                        break;
                    case 13:
                        lerCliente(scanner);
                        break;
                    case 14:
                        lerConta(scanner);
                        break;
                    case 15:
                        consultarSaldo(contaDAO, scanner);
                        break;
                    case 16:
                        criarFuncionario(scanner);
                        break;
                    case 17:
                        atualizarFuncionario(scanner);
                        break;
                    case 18:
                        lerFuncionario(scanner);
                        break;
                    case 19:
                        deletarFuncionario(scanner);
                        break;
                    case 20:
                        consultarExtrato(contaDAO, scanner);
                        break;
                    case 21:
                        gerarRelatorioMovimentacao(relatorioDAO, scanner);
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } while (option != 0);

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--------------Cliente----------------

    private static boolean login(UsuarioDAO usuarioDAO, ContaDAO contaDAO, Scanner scanner) throws SQLException {
        System.out.print("Digite o documento do cliente: ");
        String documento = scanner.next().trim();
        System.out.print("Digite a senha: ");
        String senha = scanner.next().trim();

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

        // Armazenar temporariamente os IDs e o documento
        idContaTemp = idConta;
        idUsuarioTemp = idUsuario;
        idClienteTemp = idCliente;
        documentoTemp = documento;

        System.out.println("Login realizado com sucesso!");
        return true;
    }

    private static void criarSolicitacao(SolicitacaoDAO solicitacaoDAO, UsuarioDAO usuarioDAO, ContaDAO contaDAO, Scanner scanner) throws Exception {
        System.out.print("Nome: ");
        String nome = scanner.next();
        System.out.print("Documento: ");
        String documento = scanner.next();
        if (usuarioDAO.verificarDocumentoCadastrado(documento)) {
            System.out.println("Já existe um usuário com este documento.");
            return;
        }
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Data de Nascimento (dd/MM/yyyy): ");
        String dataNascimentoString = scanner.nextLine();
        String dataNascimentoSQL = convertDateToSQLFormat(dataNascimentoString);
        System.out.print("Telefone: ");
        String telefone = scanner.next();
        String tipoUsuario = "CLIENTE"; // Definindo o tipo de usuário como "CLIENTE" por padrão
        System.out.print("Senha: ");
        String senha = scanner.next();
        System.out.println("1- Pessoa Física\n2- Pessoa Jurídica");
        int tipoPessoa = scanner.nextInt();
        boolean isPessoaFisica = (tipoPessoa == 1);
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Número da Casa: ");
        int numeroCasa;
        while (true) {
            try {
                numeroCasa = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Número da Casa inválido. Por favor, insira um número: ");
            }
        }
        System.out.print("Bairro: ");
        String bairro = scanner.next();
        System.out.print("Cidade: ");
        String cidade = scanner.next();
        System.out.print("Estado: ");
        String estado = scanner.next();
        Double saldo = 0.0; // Definindo o saldo como 0 por padrão
        System.out.print("Tipo de Conta: \n 1- Corrente\n 2- Poupança\n");
        int opcao = scanner.nextInt();

        String tipoConta = "";

        if (opcao == 1) {
            tipoConta = "CORRENTE";
        } else if (opcao == 2) {
            tipoConta = "POUPANCA";
        } else {
            System.out.println("Opção inválida!");
            return;
        }

        Conta conta = new Conta();
        String numeroConta = conta.criarNumeroConta(contaDAO);
        String agencia = "777"; // Definindo a agência como "777" por padrão

        solicitacaoDAO.criarSolicitacao(nome, documento, java.sql.Date.valueOf(dataNascimentoSQL), telefone, tipoUsuario, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, numeroConta, agencia, saldo, tipoConta);
        System.out.println("Solicitação criada com sucesso!");
    }

    private static void fazerDeposito(ContaDAO contaDAO, Scanner scanner) throws Exception {
        System.out.print("Digite o documento do cliente: ");
        String documento = scanner.next();
        if (!contaDAO.verificarDocumentoCadastrado(documento)) {
            System.out.println("Documento não encontrado.");
            return;
        }

        System.out.print("Digite o valor do depósito: ");
        double valor = scanner.nextDouble();

        // Obter o ID da conta a partir do documento
        int idConta = contaDAO.obterIdContaPorDocumento(documento);

        // Fazer depósito
        contaDAO.depositar(idConta, valor);

        System.out.println("Depósito realizado com sucesso!");
    }

    private static void fazerSaque(ContaDAO contaDAO, Scanner scanner) throws Exception {
        System.out.print("Digite o documento do cliente: ");
        String documento = scanner.next();
        if (!contaDAO.verificarDocumentoCadastrado(documento)) {
            System.out.println("Documento não encontrado.");
            return;
        }
        int idConta = contaDAO.obterIdContaPorDocumento(documento);
        System.out.print("Digite o valor do saque: ");
        double valor = scanner.nextDouble();
        contaDAO.sacar(idConta, valor);
        System.out.println("Saque realizado com sucesso!");
    }

    private static void fazerTransferencia(ContaDAO contaDAO, Scanner scanner) throws Exception {
        System.out.print("Digite o documento do cliente de origem: ");
        String documentoOrigem = scanner.next();
        if (!contaDAO.verificarDocumentoCadastrado(documentoOrigem)) {
            System.out.println("Documento de origem não encontrado.");
            return;
        }
        int idContaOrigem = contaDAO.obterIdContaPorDocumento(documentoOrigem);
        System.out.print("Digite o documento do cliente de destino: ");
        String documentoDestino = scanner.next();
        if (!contaDAO.verificarDocumentoCadastrado(documentoDestino)) {
            System.out.println("Documento de destino não encontrado.");
            return;
        }
        int idContaDestino = contaDAO.obterIdContaPorDocumento(documentoDestino);
        System.out.print("Digite o valor da transferência: ");
        double valor = scanner.nextDouble();
        contaDAO.transferir(idContaOrigem, idContaDestino, valor);
        System.out.println("Transferência realizada com sucesso!");
    }

    private static void pagarDivida(ContaDAO contaDAO, Scanner scanner) throws Exception {
        System.out.print("Digite o documento do cliente: ");
        String documento = scanner.next();
        if (!contaDAO.verificarDocumentoCadastrado(documento)) {
            System.out.println("Documento não encontrado.");
            return;
        }
        int idConta = contaDAO.obterIdContaPorDocumento(documento);
        System.out.print("Digite o valor a ser pago: ");
        double valor = scanner.nextDouble();
        contaDAO.pagarDivida(idConta, valor);
        System.out.println("Dívida paga com sucesso!");
    }

    private static void consultarSaldo(ContaDAO contaDAO, Scanner scanner) throws Exception {
        System.out.print("Digite o documento do cliente: ");
        String documento = scanner.next();
        if (!contaDAO.verificarDocumentoCadastrado(documento)) {
            System.out.println("Documento não encontrado.");
            return;
        }
        int idConta = contaDAO.obterIdContaPorDocumento(documento);
        String saldoInfo = contaDAO.verSaldo(idConta);
        System.out.println(saldoInfo);
    }

    private static void consultarExtrato(ContaDAO contaDAO, Scanner scanner) throws SQLException {
        System.out.println("Digite o ID da conta para consultar o extrato:");
        int idConta = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        List<ExtratoVO> extrato = contaDAO.consultarExtrato(idConta);

        if (extrato.isEmpty()) {
            System.out.println("Nenhuma transação encontrada para a conta.");
        } else {
            ExtratoVO primeiroItem = extrato.get(0);
            System.out.println("Nome do Dono da Conta: " + primeiroItem.getNome());
            System.out.println("Documento: " + primeiroItem.getDocumento());
            System.out.println("Número da Conta: " + primeiroItem.getNumeroConta());

            System.out.println("Transações:");
            for (ExtratoVO extratoVO : extrato) {
                System.out.println("--------------------------------------------------");
                System.out.println("Tipo de Transação: " + extratoVO.getTransacao().getTipoTransacao());
                System.out.println("Valor: " + extratoVO.getTransacao().getValor());
                System.out.println("Data/Hora: " + extratoVO.getTransacao().getDataHora());
                System.out.println("Status: " + extratoVO.getTransacao().getStatus());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    //--------------Funcionario----------------
    private static void logout() {
        // Limpar os dados temporários
        idContaTemp = 0;
        idUsuarioTemp = 0;
        idClienteTemp = 0;
        documentoTemp = null;

        System.out.println("Logout realizado com sucesso!");
        System.exit(0); // Sair do sistema
    }

    private static boolean loginFuncionario(FuncionarioDAO funcionarioDAO, UsuarioDAO usuarioDAO, Scanner scanner) throws SQLException {
        System.out.print("Digite o código do funcionário: ");
        String codigoFuncionario = scanner.next().trim();
        System.out.print("Digite a senha: ");
        String senha = scanner.next().trim();

        if (!funcionarioDAO.verificarCodigoCadastrado(codigoFuncionario)) {
            System.out.println("Código do funcionário não encontrado.");
            return false;
        }

        int idFuncionario = funcionarioDAO.obterIdFuncionarioPorCodigo(codigoFuncionario);
        int idUsuario = funcionarioDAO.obterIdUsuarioPorFuncionario(idFuncionario);
        if (!usuarioDAO.verificarSenha(idUsuario, senha)) {
            System.out.println("Senha incorreta.");
            return false;
        }
        int idCliente = funcionarioDAO.obterIdClientePorUsuario(idUsuario);
        int idConta = funcionarioDAO.obterIdContaPorCliente(idCliente);

        // Armazenar temporariamente os IDs e o código do funcionário
        idContaTemp = idConta;
        idUsuarioTemp = idUsuario;
        idClienteTemp = idCliente;
        documentoTemp = codigoFuncionario;

        System.out.println("Login realizado com sucesso!");
        return true;
    }

    private static void excluirSolicitacao(SolicitacaoDAO solicitacaoDAO, int idSolicitacao) throws SQLException {
        solicitacaoDAO.excluirSolicitacao(idSolicitacao);
        System.out.println("Solicitação excluída com sucesso!");
    }

    private static void listarSolicitacoes(SolicitacaoDAO solicitacaoDAO) throws Exception {
        ResultSet resultSet = solicitacaoDAO.listarSolicitacoes();

        // Print header
        System.out.printf("%-5s %-10s %-10s %-15s %-20s %-10s %-15s %-15s %-7s %-15s %-20s %-15s %-15s %-10s %-10s%n",
                "ID", "Documento", "Senha", "Pessoa Física", "Endereço", "Número", "Bairro", "Cidade", "Estado", "Data Nascimento", "Nome", "Telefone", "Tipo Conta", "ID Status", "Status");

        // Print rows
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

            System.out.printf("%-5d %-10s %-10s %-15b %-20s %-10s %-15s %-15s %-7s %-15s %-20s %-15s %-15s %-10d %-10s%n",
                    idSolicitacao, documento, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, dataNascimento, nome, telefone, tipoConta, idStatus, status);
        }
    }




    private static String convertDateToSQLFormat(String date) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date originalDate = originalFormat.parse(date);
        return targetFormat.format(originalDate);
    }

    private static void aprovarSolicitacao(SolicitacaoDAO solicitacaoDAO, UsuarioDAO usuarioDAO, ContaDAO contaDAO, int idSolicitacao) throws Exception {
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
                String numeroConta = rs.getString("numero_conta"); // Verifique se esta coluna está presente na consulta
                String agencia = rs.getString("agencia");
                double saldo = rs.getDouble("saldo");
                String tipoConta = rs.getString("tipo_conta");
                String status = "APROVADO";

                // Verifica se a conta tem dívidas
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



    private static void removerUsuario(UsuarioDAO usuarioDAO, Scanner scanner) throws Exception {
        System.out.print("Digite o documento do usuário a ser removido: ");
        String documentoUsuario = scanner.next();
        if (!usuarioDAO.verificarDocumentoCadastrado(documentoUsuario)) {
            System.out.println("Documento não encontrado.");
            return;
        }
        int idUsuario = usuarioDAO.obterIdUsuarioPorDocumento(documentoUsuario);
        usuarioDAO.deletarUsuario(idUsuario);
        System.out.println("Usuário removido com sucesso!");
    }

    private static void atualizarCliente(Scanner scanner) throws SQLException {
        System.out.print("ID do Cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Documento: ");
        String documento = scanner.nextLine();
        System.out.print("Data de Nascimento (yyyy-mm-dd): ");
        Date dataNascimento = Date.valueOf(scanner.nextLine());
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Número da Casa: ");
        int numeroCasa = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("Estado: ");
        String estado = scanner.nextLine();
        System.out.print("Tipo de Conta: ");
        String tipoConta = scanner.nextLine();

        funcionarioDAO.atualizarCliente(idCliente, nome, documento, dataNascimento, telefone, endereco, numeroCasa, bairro, cidade, estado, tipoConta);
        System.out.println("Cliente atualizado com sucesso.");
    }

    private static void atualizarConta(Scanner scanner) throws SQLException {
        System.out.print("ID do Usuário: ");
        int idUsuario = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

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

        System.out.println("Número da Conta: " + numeroConta);
        System.out.println("Agência: " + agencia);
        System.out.println("Saldo: " + saldo);
        System.out.println("Tipo de Conta Atual: " + tipoConta);


        String novoTipoConta = tipoConta.equals("CORRENTE") ? "POUPANCA" : (tipoConta.equals("POUPANCA") ? "CORRENTE" : tipoConta);


        if (novoTipoConta.equals(tipoConta)) {
            System.out.println("O tipo de conta é o mesmo. Nenhuma alteração será feita.");
            return;
        }

        System.out.println("Deseja alterar os dados da conta " + tipoConta + "? (true/false)");
        Boolean resposta = scanner.nextBoolean();
        updateConta(resposta, tipoConta, idUsuario, scanner);


        System.out.println("Deseja altera o tipo da conta? (true/false)");
        Boolean newResposta = scanner.nextBoolean();
        if (newResposta){
            if (novoTipoConta.equals("CORRENTE")) {
                // Se estamos mudando para corrente, precisamos de novo limite e data de vencimento
                System.out.print("Novo Limite: ");
                double novoLimite = scanner.nextDouble();
                System.out.print("Nova Data de Vencimento (yyyy-mm-dd): ");
                java.sql.Date novaDataVencimento = java.sql.Date.valueOf(scanner.next());
                scanner.nextLine(); // Consumir nova linha
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



    private static void updateConta(Boolean resposta, String tipoConta, int idUsuario, Scanner scanner) throws SQLException {
        if (tipoConta.equals("CORRENTE")) {
            System.out.print("Digite o novo limite: ");
            double novoLimite = scanner.nextDouble();
            System.out.print("Digite a nova data de vencimento (yyyy-mm-dd): ");
            java.sql.Date novaDataVencimento = java.sql.Date.valueOf(scanner.next());
            funcionarioDAO.updateConta(idUsuario, novoLimite, novaDataVencimento, 0.0, tipoConta);
        } else if (tipoConta.equals("POUPANCA")){
            System.out.print("Digite a nova taxa de rendimento: ");
            double novaTaxaRendimento = scanner.nextDouble();
            funcionarioDAO.updateConta(idUsuario, 0.0, null, novaTaxaRendimento, tipoConta);
        }
        System.out.println("Conta " + tipoConta +" atualizada com sucesso!");
    }

    private static void lerCliente(Scanner scanner) throws SQLException {
        System.out.print("ID do Cliente: ");
        int idCliente = scanner.nextInt();
        ResultSet rs = funcionarioDAO.lerClienteCompleto(idCliente);
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id_cliente"));
            System.out.println("Nome: " + rs.getString("nome"));
            System.out.println("Documento: " + rs.getString("documento"));
            System.out.println("Data de Nascimento: " + rs.getDate("data_nascimento"));
            System.out.println("Telefone: " + rs.getString("telefone"));
            System.out.println("Endereço: " + rs.getString("endereco"));
            System.out.println("Número da Casa: " + rs.getInt("numero_casa"));
            System.out.println("Bairro: " + rs.getString("bairro"));
            System.out.println("Cidade: " + rs.getString("cidade"));
            System.out.println("Estado: " + rs.getString("estado"));
            System.out.println("Tipo de Conta: " + rs.getString("tipo_conta"));
        }
    }

    private static void lerConta(Scanner scanner) throws SQLException {
        System.out.print("ID do Cliente: ");
        int idCliente = scanner.nextInt();
        ResultSet rs = funcionarioDAO.lerConta(idCliente);
        while (rs.next()) {
            System.out.println("ID da Conta: " + rs.getInt("id_conta"));
            System.out.println("Número da Conta: " + rs.getString("numero_conta"));
            System.out.println("Agência: " + rs.getString("agencia"));
            System.out.println("Saldo: " + rs.getDouble("saldo"));
            System.out.println("Tipo de Conta: " + rs.getString("tipo_conta"));
            System.out.println("Status: " + rs.getString("status"));
            if (rs.getString("tipo_conta").equals("CORRENTE")) {
                System.out.println("Limite: " + rs.getDouble("limite"));
                System.out.println("Data de Vencimento: " + rs.getDate("data_vencimento"));
                System.out.println("Dívida: " + rs.getDouble("divida"));
            } else if (rs.getString("tipo_conta").equals("POUPANCA")) {
                System.out.println("Taxa de Rendimento: " + rs.getDouble("taxa_rendimento"));
            }
        }
    }

    private static void criarFuncionario(Scanner scanner) throws SQLException {
        System.out.print("Nome: ");
        String nome = scanner.next();

        System.out.print("Documento: ");
        String documento = scanner.next();

        System.out.print("Data de Nascimento (YYYY-MM-DD): ");
        String dataNascimentoStr = scanner.next();
        Date dataNascimento = Date.valueOf(dataNascimentoStr);

        System.out.print("Telefone: ");
        String telefone = scanner.next();

        System.out.print("Cargo: ");
        String cargo = scanner.next();

        String codigoFuncionario = funcionarioDAO.gerarCodigoFuncionario();

        System.out.print("Senha: ");
        String senha = scanner.next();

        System.out.print("CEP: ");
        String cep = scanner.next();

        System.out.print("Local: ");
        String local = scanner.next();

        System.out.print("Número da Casa: ");
        int numeroCasa = scanner.nextInt();

        System.out.print("Bairro: ");
        String bairro = scanner.next();

        System.out.print("Cidade: ");
        String cidade = scanner.next();

        System.out.print("Estado: ");
        String estado = scanner.next();

        System.out.print("Salário: ");
        double salario = scanner.nextDouble();

        Conta conta = new Conta();
        String numeroContaPoupanca = conta.criarNumeroConta(contaDAO);
        funcionarioDAO.criarFuncionario(nome, documento, dataNascimento, telefone, cargo, codigoFuncionario, senha, cep, local, numeroCasa, bairro, cidade, estado, salario, numeroContaPoupanca);
        System.out.println("Funcionário criado com sucesso. Número da conta poupança: " + numeroContaPoupanca);
    }

    private static void deletarFuncionario(Scanner scanner) throws SQLException {
        System.out.print("ID do Funcionario: ");
        int idFuncionario = scanner.nextInt();
        funcionarioDAO.deletarFuncionario(idFuncionario);
        System.out.println("Funcionário deletado com sucesso.");
    }

    private static void lerFuncionario(Scanner scanner) throws SQLException {
        System.out.print("ID do Funcionario: ");
        int idFuncionario = scanner.nextInt();
        ResultSet resultSet = funcionarioDAO.lerFuncionario(idFuncionario);
        if (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt("id_funcionario"));
            System.out.println("Código do Funcionário: " + resultSet.getString("codigo_funcionario"));
            System.out.println("Cargo: " + resultSet.getString("cargo"));
            System.out.println("Data de Nascimento (Funcionário): " + resultSet.getDate("data_nascimento_funcionario"));
            System.out.println("Nome: " + resultSet.getString("nome"));
            System.out.println("Documento: " + resultSet.getString("documento"));
            System.out.println("Data de Nascimento (Usuário): " + resultSet.getDate("data_nascimento_usuario"));
            System.out.println("Telefone: " + resultSet.getString("telefone"));
            System.out.println("Senha: " + resultSet.getString("senha"));
            System.out.println("CEP: " + resultSet.getString("cep"));
            System.out.println("Local: " + resultSet.getString("local"));
            System.out.println("Número da Casa: " + resultSet.getInt("numero_casa"));
            System.out.println("Bairro: " + resultSet.getString("bairro"));
            System.out.println("Cidade: " + resultSet.getString("cidade"));
            System.out.println("Estado: " + resultSet.getString("estado"));
            System.out.println("ID do Cliente: " + resultSet.getInt("id_cliente"));
        } else {
            System.out.println("Funcionário não encontrado.");
        }
    }

    private static void atualizarFuncionario(Scanner scanner) throws SQLException {
        System.out.print("ID do Funcionario: ");
        int idFuncionario = scanner.nextInt();

        scanner.nextLine();  // Consome a nova linha
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Documento: ");
        String documento = scanner.nextLine();

        System.out.print("Data de Nascimento (YYYY-MM-DD): ");
        String dataNascimentoStr = scanner.nextLine();
        Date dataNascimento = Date.valueOf(dataNascimentoStr);

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Cargo: ");
        String cargo = scanner.nextLine();

        ResultSet resultSet = funcionarioDAO.lerFuncionario(idFuncionario);
        String codigoFuncionario = resultSet.getString("codigo_funcionario");

        System.out.print("CEP: ");
        String cep = scanner.nextLine();

        System.out.print("Local: ");
        String local = scanner.nextLine();

        System.out.print("Número da Casa: ");
        int numeroCasa = scanner.nextInt();

        scanner.nextLine();  // Consume newline
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();

        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        System.out.print("Estado: ");
        String estado = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        funcionarioDAO.atualizarFuncionario(idFuncionario, nome, documento, dataNascimento, telefone, cargo, codigoFuncionario, senha, cep, local, numeroCasa, bairro, cidade, estado);
        System.out.println("Funcionário atualizado com sucesso.");
    }


    private static void gerarRelatorioMovimentacao(RelatorioDAO relatorioDAO, Scanner scanner) throws SQLException, IOException, MessagingException, GeneralSecurityException {
        System.out.println("1- Relatorio Conta\n2- Relatorio Usuario\n3- Relatorio Geral");
        int tipoRelatorio = scanner.nextInt();
        String conteudo = "";

        if (tipoRelatorio == 1) {
            System.out.println("Digite o ID da conta para gerar o relatório de movimentação:");
            int idConta = scanner.nextInt();
            conteudo = relatorioDAO.gerarRelatorioMovimentacao(idConta);
        } else if (tipoRelatorio == 2) {
            System.out.println("Digite o ID do usuário para gerar o relatório de informações:");
            int idUsuario = scanner.nextInt();
            conteudo = relatorioDAO.gerarRelatorioInformacoes(idUsuario);
        } else if (tipoRelatorio == 3) {
            System.out.println("Digite o ID da conta para gerar o relatório geral:");
            int idConta = scanner.nextInt();
            System.out.println("Digite o ID do usuário para gerar o relatório geral:");
            int idUsuario = scanner.nextInt();
            conteudo = relatorioDAO.gerarRelatorioGeral(idConta, idUsuario);
        }

        System.out.println("Deseja exportar o relatório para Excel? (true/false)");
        boolean exportar = scanner.nextBoolean();
        if (exportar) {
            System.out.println("Digite o caminho do diretório para exportar:");
            String caminhoDiretorio = scanner.next();
            relatorioDAO.exportarRelatorioParaExcel(conteudo, caminhoDiretorio);
        }
    }
}