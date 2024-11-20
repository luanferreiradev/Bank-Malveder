package model;

import model.enums.Status_Solicitacao;
import model.impl.ClientePF;
import model.impl.ContaCorrente;
import model.impl.ContaPoupanca;
import model.service.BancoServico;
import model.service.Transacao;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Banco {
    private static BancoServico bancoServico;
    private static Scanner scanner = new Scanner(System.in);
    private static Funcionario funcionarioLogado;

    public static void main(String[] args) {
        bancoServico = new BancoServico("Banco", LocalDate.now(), "0000-0000", "senhaBanco", "00000-000", "Local", "123", "Bairro", "Cidade", "Estado");


        // Criar funcionário root
        Funcionario root = new Funcionario("root", LocalDate.now(), "0000000000", "root", "00000-000", "Local", "0", "Bairro", "Cidade", "Estado", "0001", "Administrador", bancoServico);
        bancoServico.adicionarFuncionario(root);

        while (true) {
            System.out.println("Menu Inicial:");
            System.out.println("1. Funcionário");
            System.out.println("2. Cliente");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    menuFuncionario();
                    break;
                case 2:
                    menuCliente();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuFuncionario() {
        while (true) {
            System.out.println("Menu Funcionário:");
            System.out.println("1. Fazer Login");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    if (loginFuncionario()) {
                        menuFuncionarioLogado();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static boolean loginFuncionario() {
        System.out.print("Código do Funcionário: ");
        String codigoFuncionario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Funcionario funcionario = bancoServico.getFuncionario(codigoFuncionario);
        if (funcionario != null && funcionario.login(senha)) {
            funcionarioLogado = funcionario;
            if (codigoFuncionario.equals("0001") && senha.equals("root")) {
                menuRoot();
            } else {
                menuFuncionarioLogado();
            }
            return true;
        } else {
            System.out.println("Código ou senha inválidos!");
            return false;
        }
    }

    private static void menuRoot() {
        while (true) {
            System.out.println("Menu Root:");
            System.out.println("1. Cliente");
            System.out.println("2. Funcionário");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    menuClienteRoot();
                    break;
                case 2:
                    menuFuncionarioRoot();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuClienteRoot() {
        while (true) {
            System.out.println("Menu Cliente Root:");
            System.out.println("1. Criar Cliente");
            System.out.println("2. Remover Cliente");
            System.out.println("3. Consultar Dados da Conta");
            System.out.println("4. Consultar Dados do Cliente");
            System.out.println("5. Alterar Dados da Conta");
            System.out.println("6. Alterar Dados do Cliente");
            System.out.println("7. Aprovar Solicitação");
            System.out.println("8. Rejeitar Solicitação");
            System.out.println("9. Listar Solicitações");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    criarSolicitacao();
                    break;
                case 2:
                    removerCliente();
                    break;
                case 3:
                    consultarDadosConta();
                    break;
                case 4:
                    consultarDadosCliente();
                    break;
                case 5:
                    alterarDadosConta();
                    break;
                case 6:
                    alterarDadosCliente();
                    break;
                case 7:
                    aprovarSolicitacao();
                    break;
                case 8:
                    rejeitarSolicitacao();
                    break;
                case 9:
                    listarSolicitacoes();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuFuncionarioRoot() {
        while (true) {
            System.out.println("Menu Funcionário Root:");
            System.out.println("1. Gerar Relatório de Movimentação");
            System.out.println("2. Criar Funcionário");
            System.out.println("3. Remover Funcionário");
            System.out.println("4. Consultar Dados do Funcionário");
            System.out.println("5. Alterar Dados do Funcionário");
            System.out.println("6. Listar Funcionários");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    geraRelatorioMovimentacao();
                    break;
                case 2:
                    criarNovoFuncionario();
                    break;
                case 3:
                    removerFuncionario();
                    break;
                case 4:
                    consultarDadosFuncionario();
                    break;
                case 5:
                    alterarDadosFuncionario();
                    break;
                case 6:
                    listarFuncionarios();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuFuncionarioLogado() {
        while (true) {
            System.out.println("Menu do Funcionário:");
            System.out.println("1. Abrir Conta");
            System.out.println("2. Encerrar Conta");
            System.out.println("3. Consultar Dados da Conta");
            System.out.println("4. Consultar Dados do Cliente");
            System.out.println("5. Alterar Dados da Conta");
            System.out.println("6. Alterar Dados do Cliente");
            System.out.println("7. Cadastrar Funcionário");
            System.out.println("8. Gerar Relatório de Movimentação");
            System.out.println("9. Listar Solicitações");
            System.out.println("10. Aprovar Solicitação");
            System.out.println("11. Rejeitar Solicitação");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    criarSolicitacao();
                    break;
                case 2:
                    encerrarConta();
                    break;
                case 3:
                    consultarDadosConta();
                    break;
                case 4:
                    consultarDadosCliente();
                    break;
                case 5:
                    alterarDadosConta();
                    break;
                case 6:
                    alterarDadosCliente();
                    break;
                case 7:
                    criarNovoFuncionario();
                    break;
                case 8:
                    geraRelatorioMovimentacao();
                    break;
                case 9:
                    listarSolicitacoes();
                    break;
                case 10:
                    aprovarSolicitacao();
                    break;
                case 11:
                    rejeitarSolicitacao();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }


    public static void encerrarConta() {
        System.out.print("Número da conta a ser encerrada: ");
        int numeroConta = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        if (funcionarioLogado != null) {
            funcionarioLogado.removerConta(numeroConta);

            // Verificar se a conta foi removida
            Conta conta = bancoServico.getConta(numeroConta);
            if (conta == null) {
                System.out.println("Conta encerrada com sucesso!");
            } else {
                System.out.println("Erro ao encerrar a conta. Tente novamente.");
            }
        } else {
            System.out.println("Nenhum funcionário logado.");
        }
    }

    // Banco.java
    // Banco.java
    public static void consultarDadosConta() {
        System.out.print("Número da conta: ");
        long numeroConta = scanner.nextLong();
        scanner.nextLine(); // Consumir a nova linha

        Conta conta = bancoServico.getConta((int) numeroConta);
        if (conta != null) {
            System.out.println("Número da Conta: " + conta.getNumeroConta());
            System.out.println("Tipo de Conta: " + (conta instanceof ContaCorrente ? "Corrente" : "Poupança"));
            System.out.println("Saldo: " + conta.consultaSaldo());
            if (conta instanceof ContaCorrente) {
                ContaCorrente contaCorrente = (ContaCorrente) conta;
                System.out.println("Limite: " + contaCorrente.consultarLimite());
                System.out.println("Data de Vencimento: " + contaCorrente.getDataVencimento());
            }
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    // Banco.java
    public static void consultarDadosCliente() {
        System.out.print("Documento do cliente: ");
        String documento = scanner.nextLine();

        Cliente cliente = bancoServico.getCliente(documento);
        if (cliente != null) {
            System.out.println("Nome: " + cliente.getNome());
            System.out.println("Telefone: " + cliente.getTelefone());
            System.out.println("Cidade: " + cliente.getCidade());
            System.out.println("Estado: " + cliente.getEstado());
            System.out.println("Data de Nascimento: " + cliente.getDataNascimento());
            System.out.println("Tipo de Cliente: " + (cliente instanceof ClientePF ? "Pessoa Física" : "Pessoa Jurídica"));
            System.out.println("Documento: " + cliente.getDocumento());
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    // Banco.java
    private static void alterarDadosConta() {
        System.out.print("Número da conta: ");
        long numeroConta = scanner.nextLong();
        scanner.nextLine(); // Consumir a nova linha

        Conta conta = bancoServico.getConta((int) numeroConta);
        if (conta != null) {
            if (conta instanceof ContaCorrente) {
                alterarDadosContaCorrente((ContaCorrente) conta);
            } else if (conta instanceof ContaPoupanca) {
                alterarDadosContaPoupanca((ContaPoupanca) conta);
            }
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    private static void alterarDadosContaCorrente(ContaCorrente contaCorrente) {
        while (true) {
            System.out.println("Menu de Alteração - Conta Corrente:");
            System.out.println("1. Alterar Limite da Conta");
            System.out.println("2. Alterar Data de Vencimento");
            System.out.println("3. Alterar Tipo de Conta para Poupança");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    System.out.print("Novo Limite: ");
                    double novoLimite = scanner.nextDouble();
                    scanner.nextLine(); // Consumir a nova linha
                    contaCorrente.setLimite(novoLimite);
                    System.out.println("Limite alterado com sucesso!");
                    break;
                case 2:
                    System.out.print("Nova Data de Vencimento (yyyy-MM-dd): ");
                    String novaData = scanner.nextLine();
                    contaCorrente.setDataVencimento(LocalDate.parse(novaData));
                    System.out.println("Data de vencimento alterada com sucesso!");
                    break;
                case 3:
                    if (contaCorrente.getDivida() == 0) {
                        bancoServico.alterarTipoConta(contaCorrente, "Poupança");
                        //System.out.println("Tipo de conta alterado para: Poupança");
                        return; // Sair do menu após a alteração
                    } else {
                        System.out.println("Pendências a serem resolvidas!");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void alterarDadosContaPoupanca(ContaPoupanca contaPoupanca) {
        while (true) {
            System.out.println("Menu de Alteração - Conta Poupança:");
            System.out.println("1. Alterar Taxa de Rendimento");
            System.out.println("2. Alterar Tipo de Conta para Corrente");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    System.out.print("Nova Taxa de Rendimento: ");
                    double novaTaxa = scanner.nextDouble();
                    scanner.nextLine(); // Consumir a nova linha
                    contaPoupanca.setTaxaRendimento(novaTaxa);
                    System.out.println("Taxa de rendimento alterada com sucesso!");
                    break;
                case 2:
                    bancoServico.alterarTipoConta(contaPoupanca, "Corrente");
                    //System.out.println("Tipo de conta alterado para: Corrente");
                    return; // Sair do menu após a alteração
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public static void alterarDadosCliente() {
        System.out.print("Documento do cliente: ");
        String documento = scanner.nextLine();

        Cliente cliente = bancoServico.getCliente(documento);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine();
        System.out.print("Novo telefone: ");
        String novoTelefone = scanner.nextLine();
        System.out.print("Nova senha: ");
        String novaSenha = scanner.nextLine();
        System.out.print("Novo CEP: ");
        String novoCep = scanner.nextLine();
        System.out.print("Novo local: ");
        String novoLocal = scanner.nextLine();
        System.out.print("Novo bairro: ");
        String novoBairro = scanner.nextLine();
        System.out.print("Nova cidade: ");
        String novaCidade = scanner.nextLine();
        System.out.print("Novo estado: ");
        String novoEstado = scanner.nextLine();
        System.out.print("Nova data de nascimento (dd/MM/yyyy): ");
        String dataNascimentoStr = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate novaDataNascimento;
        try {
            novaDataNascimento = LocalDate.parse(dataNascimentoStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            return;
        }

        System.out.print("É pessoa física (true/false): ");
        boolean isPessoaFisica = Boolean.parseBoolean(scanner.nextLine());

        if (funcionarioLogado != null) {
            funcionarioLogado.alterarDadosCliente(documento, novoNome, novoTelefone, novaSenha, novoCep, novoLocal, novoBairro, novaCidade, novoEstado, novaDataNascimento, isPessoaFisica);
        } else {
            System.out.println("Nenhum funcionário logado.");
        }
    }

    private static void criarNovoFuncionario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Data de Nascimento (dd/MM/yyyy): ");
        String dataDeNascimentoStr = scanner.nextLine();
        LocalDate dataDeNascimento = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            dataDeNascimento = LocalDate.parse(dataDeNascimentoStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            return;
        }

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("CEP: ");
        String cep = scanner.nextLine();
        System.out.print("Local: ");
        String local = scanner.nextLine();
        System.out.print("Número da Casa: ");
        String numeroCasa = scanner.nextLine();
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("Estado: ");
        String estado = scanner.nextLine();
        String codigoFuncionario = BancoServico.gerarProximoCodigoFuncionario();
        System.out.print("Cargo: ");
        String cargo = scanner.nextLine();

        if (funcionarioLogado != null) {
            funcionarioLogado.cadastrarUmFuncionario(nome, dataDeNascimento, telefone, senha, cep, local, numeroCasa, bairro, cidade, estado, codigoFuncionario, cargo);
            System.out.println("Funcionário cadastrado com sucesso. Código do Funcionário: " + codigoFuncionario);
        } else {
            System.out.println("Nenhum funcionário logado.");
        }
    }

    private static void geraRelatorioMovimentacao() {
        System.out.print("Número da conta: ");
        int numeroConta = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        if (funcionarioLogado != null) {
            Conta conta = bancoServico.getConta(numeroConta);
            if (conta != null) {
                Cliente cliente = bancoServico.getCliente(conta.getCliente().getDocumento());
                String nomeUsuario = cliente != null ? cliente.getNome() : "Desconhecido";
                String tipoConta = conta instanceof ContaCorrente ? "Corrente" : "Poupança";

                List<String> dados = new ArrayList<>();
                for (Transacao transacao : conta.consultarExtrato()) {
                    dados.add("Data: " + transacao.getDataHora() + ", Tipo: " + transacao.getTipo() + ", Valor: " + transacao.getValor());
                }

                Relatorio relatorio = new Relatorio("Movimentações da Conta", dados, nomeUsuario, tipoConta);
                funcionarioLogado.getRelatorios().add(relatorio);

                System.out.println(relatorio.toString());
                System.out.println("Deseja exportar o relatório para Excel? (sim/não)");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("sim")) {
                    System.out.print("Diretório para salvar o arquivo: ");
                    String diretorio = scanner.nextLine();
                    String filePath = diretorio + "/relatorio.xls";
                    try {
                        relatorio.exportarParaExcel(filePath);
                        System.out.println("Relatório exportado com sucesso.");
                    } catch (IOException e) {
                        System.out.println("Erro ao exportar o relatório: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Conta não encontrada.");
            }
        } else {
            System.out.println("Nenhum funcionário logado.");
        }
    }

    private static void menuCliente() {
        while (true) {
            System.out.println("Menu Cliente:");
            System.out.println("1. Fazer Login");
            System.out.println("2. Cadastrar Novo Cliente");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    if (loginCliente()) {
                        menuClienteLogado();
                    }
                    break;
                case 2:
                    criarSolicitacao();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static Cliente clienteLogado;

    // Banco.java
    private static boolean loginCliente() {
        System.out.print("Documento do Cliente: ");
        String documento = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Cliente cliente = bancoServico.getCliente(documento);
        if (cliente != null && cliente.login(senha)) {
            Conta conta = bancoServico.getContaPorCliente(cliente);
            if (conta != null && conta.getStatus().equals(Status_Solicitacao.APROVADO)) {
                clienteLogado = cliente; // Definir o cliente logado
                System.out.println("Login bem-sucedido!");
                return true;
            } else {
                System.out.println("Conta não aprovada ou não encontrada!");
                return false;
            }
        } else {
            System.out.println("Documento ou senha inválidos!");
            return false;
        }
    }


    private static void menuClienteLogado() {
        while (true) {
            System.out.println("Menu do Cliente:");
            System.out.println("1. Consultar Saldo");
            System.out.println("2. Depositar Valor");
            System.out.println("3. Sacar Valor");
            System.out.println("4. Consultar Extrato");
            Conta conta = bancoServico.getContaPorCliente(clienteLogado);
            if (conta instanceof ContaCorrente) {
                System.out.println("5. Consultar Limite");
            } else if (conta instanceof ContaPoupanca) {
                System.out.println("5. Consultar Taxa de Rendimento");
            }
            System.out.println("6. Transferir Valor");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    consultarSaldo();
                    break;
                case 2:
                    depositarValor();
                    break;
                case 3:
                    sacarValor();
                    break;
                case 4:
                    consultarExtrato();
                    break;
                case 5:
                    if (conta instanceof ContaCorrente) {
                        consultarLimite();
                    } else if (conta instanceof ContaPoupanca) {
                        consultarTaxaRendimento();
                    }
                    break;
                case 6:
                    transferirValor();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void consultarTaxaRendimento() {
        System.out.print("Número da conta: ");
        long numeroConta = scanner.nextLong();
        scanner.nextLine(); // Consumir a nova linha

        Conta conta = bancoServico.getConta((int) numeroConta);
        if (conta != null && conta instanceof ContaPoupanca) {
            ContaPoupanca contaPoupanca = (ContaPoupanca) conta;
            double taxaRendimento = contaPoupanca.getTaxaRendimento();
            System.out.println("Taxa de Rendimento Mensal: " + ((taxaRendimento * 100) / 12) + "%");
        } else {
            System.out.println("Conta não encontrada ou não é uma conta poupança!");
        }
    }

    // Métodos existentes como consultarSaldo, depositarValor, etc.
    private static void consultarSaldo() {
        System.out.print("Número da conta: ");
        long numeroConta = scanner.nextLong();
        scanner.nextLine(); // Consumir a nova linha

        Conta conta = bancoServico.getConta((int) numeroConta);
        if (conta != null) {
            System.out.println("Saldo: " + conta.consultaSaldo());
            if (conta instanceof ContaCorrente) {
                ContaCorrente contaCorrente = (ContaCorrente) conta;
                System.out.println("Dívida: " + contaCorrente.getDivida());
            }
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    private static void depositarValor() {
        System.out.print("Número da conta: ");
        long numeroConta = scanner.nextLong();
        System.out.print("Valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir a nova linha

        Conta conta = bancoServico.getConta((int) numeroConta);
        if (conta != null) {
            conta.deposito(valor);
            System.out.println("Depósito realizado com sucesso!");
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    private static void sacarValor() {
        System.out.print("Número da conta: ");
        long numeroConta = scanner.nextLong();
        System.out.print("Valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir a nova linha

        Conta conta = bancoServico.getConta((int) numeroConta);
        if (conta != null) {
            Status_Solicitacao status = conta.saque(valor);
            System.out.println("Status do saque: " + status);
            if (conta instanceof ContaCorrente) {
                ContaCorrente contaCorrente = (ContaCorrente) conta;
                System.out.println("Novo saldo: " + contaCorrente.consultaSaldo());
                System.out.println("Novo limite: " + contaCorrente.consultarLimite());
            }
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    private static void consultarExtrato() {
        System.out.print("Número da conta: ");
        long numeroConta = scanner.nextLong();
        scanner.nextLine(); // Consumir a nova linha

        Conta conta = bancoServico.getConta((int) numeroConta);
        if (conta != null) {
            List<Transacao> extrato = conta.consultarExtrato();
            extrato.forEach(System.out::println);
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    private static void consultarLimite() {
        System.out.print("Número da conta: ");
        long numeroConta = scanner.nextLong();
        scanner.nextLine(); // Consumir a nova linha

        Conta conta = bancoServico.getConta((int) numeroConta);
        if (conta != null) {
            if (conta instanceof ContaCorrente) {
                ContaCorrente contaCorrente = (ContaCorrente) conta;
                System.out.println("Limite: " + contaCorrente.consultarLimite());
            } else {
                System.out.println("Esta conta não possui limite.");
            }
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    private static void transferirValor() {
        System.out.print("Número da conta de origem: ");
        long numeroContaOrigem = scanner.nextLong();
        System.out.print("Número da conta de destino: ");
        long numeroContaDestino = scanner.nextLong();
        System.out.print("Valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir a nova linha

        Conta contaOrigem = bancoServico.getConta((int) numeroContaOrigem);
        Conta contaDestino = bancoServico.getConta((int) numeroContaDestino);
        if (contaOrigem != null && contaDestino != null) {
            Status_Solicitacao status = contaOrigem.transferencia(contaDestino, valor);
            System.out.println("Status da transferência: " + status);
        } else {
            System.out.println("Conta de origem ou destino não encontrada!");
        }
    }

    // Banco.java
    // Banco.java
    private static void criarSolicitacao() {
        System.out.print("Documento: ");
        String documento = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Pessoa Física (true/false): ");
        boolean isPessoaFisica = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a nova linha
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Número da casa: ");
        String numeroCasa = scanner.nextLine();
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("Estado: ");
        String estado = scanner.nextLine();
        System.out.print("Data de Nascimento (dd/MM/yyyy): ");
        String dataNascimento = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.println("Selecione o tipo de conta:");
        System.out.println("1. Corrente");
        System.out.println("2. Poupança");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha
        String tipoConta = opcao == 1 ? "Corrente" : "Poupança";

        bancoServico.criarSolicitacao(documento, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, dataNascimento, nome, telefone, tipoConta);
        System.out.println("Solicitação criada com sucesso!");
    }

    private static void listarSolicitacoes() {
        List<Solicitacao> solicitacoes = bancoServico.listarSolicitacoes();
        solicitacoes.forEach(System.out::println);
    }

    private static void aprovarSolicitacao() {
        System.out.print("ID da solicitação: ");
        int idSolicitacao = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        bancoServico.aprovarSolicitacao(idSolicitacao);
        System.out.println("Solicitação aprovada!");
    }


    private static void rejeitarSolicitacao() {
        System.out.print("ID da solicitação: ");
        int idSolicitacao = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        bancoServico.rejeitarSolicitacao(idSolicitacao);
        System.out.println("Solicitação rejeitada!");
    }

    private static void removerCliente() {
        System.out.print("Documento do cliente: ");
        String documento = scanner.nextLine();
        Cliente cliente = bancoServico.getCliente(documento);
        if (cliente != null) {
            bancoServico.removerCliente(cliente);
            System.out.println("Cliente removido com sucesso!");
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    private static void removerFuncionario() {
        System.out.print("Código do Funcionário: ");
        String codigoFuncionario = scanner.nextLine();
        bancoServico.removerFuncionario(codigoFuncionario);
        System.out.println("Funcionário removido com sucesso!");
    }

    private static void consultarDadosFuncionario() {
        System.out.print("Código do Funcionário: ");
        String codigoFuncionario = scanner.nextLine();
        Funcionario funcionario = bancoServico.getFuncionario(codigoFuncionario);
        if (funcionario != null) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Telefone: " + funcionario.getTelefone());
            System.out.println("Cidade: " + funcionario.getCidade());
            System.out.println("Estado: " + funcionario.getEstado());
            System.out.println("Data de Nascimento: " + funcionario.getDataDeNascimento());
            System.out.println("Cargo: " + funcionario.getCargo());
        } else {
            System.out.println("Funcionário não encontrado.");
        }
    }

    private static void alterarDadosFuncionario() {
        System.out.print("Código do Funcionário: ");
        String codigoFuncionario = scanner.nextLine();
        Funcionario funcionario = bancoServico.getFuncionario(codigoFuncionario);
        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine();
        System.out.print("Novo telefone: ");
        String novoTelefone = scanner.nextLine();
        System.out.print("Nova senha: ");
        String novaSenha = scanner.nextLine();
        System.out.print("Novo CEP: ");
        String novoCep = scanner.nextLine();
        System.out.print("Novo local: ");
        String novoLocal = scanner.nextLine();
        System.out.print("Novo bairro: ");
        String novoBairro = scanner.nextLine();
        System.out.print("Nova cidade: ");
        String novaCidade = scanner.nextLine();
        System.out.print("Novo estado: ");
        String novoEstado = scanner.nextLine();
        System.out.print("Nova data de nascimento (dd/MM/yyyy): ");
        String dataNascimentoStr = scanner.nextLine();
        System.out.print("Número da Casa: ");
        String numeroCasa = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate novaDataNascimento;
        try {
            novaDataNascimento = LocalDate.parse(dataNascimentoStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            return;
        }

        bancoServico.alterarDadosFuncionario(codigoFuncionario, novoNome, novoTelefone, novaSenha, novoCep, novoLocal, novoBairro, novaCidade, novoEstado, novaDataNascimento, numeroCasa);
        System.out.println("Dados do funcionário alterados com sucesso!");
    }

    private static void listarFuncionarios() {
        List<String> funcionarios = bancoServico.listarFuncionarios();
        funcionarios.forEach(System.out::println);
    }
}