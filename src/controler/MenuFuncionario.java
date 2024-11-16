package controler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import model.dao.enties.impl.Cliente;
import model.dao.enties.impl.ContaCorrente;
import model.dao.enties.impl.ContaPoupanca;
import model.dao.enties.impl.Funcionario;
import model.dao.enties.impl.Relatorio;

public class MenuFuncionario {

    private Funcionario funcionarioLogado;

    public MenuFuncionario(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }

    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("===== Menu Funcionario =====");
            System.out.println("1. Abertura de Conta");
            System.out.println("2. Encerramento de Conta");
            System.out.println("3. Consulta de Dados");
            System.out.println("4. Alteração de Dados");
            System.out.println("5. Cadastro de Funcionários");
            System.out.println("6. Geração de Relatórios");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    aberturaDeConta(scanner);
                    break;
                case 2:
                    encerramentoDeConta(scanner);
                    break;
                case 3:
                    consultaDeDados(scanner);
                    break;
                case 4:
                    alteracaoDeDados(scanner);
                    break;
                case 5:
                    cadastroDeFuncionarios(scanner);
                    break;
                case 6:
                    geracaoDeRelatorios(scanner);
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 7);

        scanner.close();
    }

    private void aberturaDeConta(Scanner scanner) {
        System.out.println("===== Abertura de Conta =====");
        System.out.print("Tipo de Conta (poupanca/corrente): ");
        String tipoConta = scanner.next();

        System.out.print("Nome do Cliente: ");
        String nome = scanner.next();
        System.out.print("CPF: ");
        String cpf = scanner.next();
        System.out.print("Data de Nascimento (AAAA-MM-DD): ");
        String dataNascimento = scanner.next();
        System.out.print("Telefone: ");
        String telefone = scanner.next();
        System.out.print("Endereço (CEP, Local, Número da Casa, Bairro, Cidade, Estado): ");
        scanner.nextLine();
        String endereco = scanner.nextLine();
        System.out.print("Senha do Cliente: ");
        String senha = scanner.next();
        
        if (tipoConta.equalsIgnoreCase("poupanca")) {
        	Cliente cliente = new Cliente(null, senha);
            ContaPoupanca contaPoupanca = (ContaPoupanca) cliente.criarConta(tipoConta, null, null, contaPoupanca.getTaxaDeRendimento(), null);
            funcionarioLogado.abrirConta(contaPoupanca, cliente);
        } else if (tipoConta.equalsIgnoreCase("corrente")) {
            System.out.print("Limite da Conta: ");
            double limite = scanner.nextDouble();
            System.out.print("Data de Vencimento (MM-DD-AAAA): ");
            String dataVencimento = scanner.next();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate localDate = LocalDate.parse(dataVencimento, formatter);
            
            Cliente cliente = new Cliente(null, senha);
            ContaCorrente contaCorrente = (ContaCorrente) cliente.criarConta(tipoConta, null, null, limite, localDate);
            funcionarioLogado.abrirConta(contaCorrente, cliente);
        } else {
            System.out.println("Tipo de conta inválido.");
        }
    }

    private void encerramentoDeConta(Scanner scanner) {
        System.out.println("===== Encerramento de Conta =====");
        System.out.print("Senha de Administrador: ");
        String senhaAdmin = scanner.next();

        if (senhaAdmin.equals("admin123")) { // "admin123" é a senha do administrador
            System.out.print("Número da Conta: ");
            int numeroConta = scanner.nextInt();
            // lógica para encerrar conta
            System.out.println("Conta encerrada com sucesso.");
        } else {
            System.out.println("Senha de administrador inválida.");
        }
    }

    private void consultaDeDados(Scanner scanner) {
        System.out.println("===== Consulta de Dados =====");
        System.out.println("1. Consultar Conta");
        System.out.println("2. Consultar Funcionário");
        System.out.println("3. Consultar Cliente");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                consultarConta(scanner);
                break;
            case 2:
                consultarFuncionario(scanner);
                break;
            case 3:
                consultarCliente(scanner);
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void consultarConta(Scanner scanner) {
        System.out.print("Número da Conta: ");
        int numeroConta = scanner.nextInt();
        // lógica para consultar conta
        System.out.println("Dados da conta: [exemplo de dados da conta]");
    }

    private void consultarFuncionario(Scanner scanner) {
        System.out.print("Código do Funcionário: ");
        String codigoFuncionario = scanner.next();
        // lógica para consultar funcionário
        System.out.println("Dados do funcionário: [exemplo de dados do funcionário]");
    }

    private void consultarCliente(Scanner scanner) {
        System.out.print("ID do Cliente: ");
        int idCliente = scanner.nextInt();
        // lógica para consultar cliente
        System.out.println("Dados do cliente: [exemplo de dados do cliente]");
    }

    private void alteracaoDeDados(Scanner scanner) {
        System.out.println("===== Alteração de Dados =====");
        System.out.println("1. Alterar Conta");
        System.out.println("2. Alterar Funcionário");
        System.out.println("3. Alterar Cliente");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                alterarConta(scanner);
                break;
            case 2:
                alterarFuncionario(scanner);
                break;
            case 3:
                alterarCliente(scanner);
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void alterarConta(Scanner scanner) {
        System.out.print("Número da Conta: ");
        int numeroConta = scanner.nextInt();
        // lógica para alterar dados da conta
        System.out.println("Dados da conta alterados com sucesso.");
    }

    private void alterarFuncionario(Scanner scanner) {
        System.out.print("Código do Funcionário: ");
        String codigoFuncionario = scanner.next();
        // lógica para alterar dados do funcionário
        System.out.println("Dados do funcionário alterados com sucesso.");
    }

    private void alterarCliente(Scanner scanner) {
        System.out.print("ID do Cliente: ");
        int idCliente = scanner.nextInt();
        // lógica para alterar dados do cliente
        System.out.println("Dados do cliente alterados com sucesso.");
    }

    private void cadastroDeFuncionarios(Scanner scanner) {
        System.out.println("===== Cadastro de Funcionários =====");
        System.out.print("Senha de Administrador: ");
        String senhaAdmin = scanner.next();

        if (senhaAdmin.equals("admin123")) { // Supondo que "admin123" é a senha do administrador
            System.out.print("Código do Funcionário: ");
            String codigoFuncionario = scanner.next();
            System.out.print("Cargo: ");
            String cargo = scanner.next();
            System.out.print("Nome: ");
            String nome = scanner.next();
            System.out.print("CPF: ");
            String cpf = scanner.next();
            System.out.print("Data de Nascimento (AAAA-MM-DD): ");
            String dataNascimento = scanner.next();
            System.out.print("Telefone: ");
            String telefone = scanner.next();
            System.out.print("Endereço (CEP, Local, Número da Casa, Bairro, Cidade, Estado): ");
            scanner.nextLine();
            String endereco = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.next();

            Funcionario novoFuncionario = new Funcionario(codigoFuncionario, cargo, cpf, dataNascimento, telefone, endereco, senha);
            funcionarioLogado.cadastrarFuncionario(novoFuncionario);
            System.out.println("Funcionário cadastrado com sucesso.");
        } else {
            System.out.println("Senha de administrador inválida.");
        }
    }

    private void geracaoDeRelatorios(Scanner scanner) {
        System.out.println("===== Geração de Relatórios =====");
        System.out.print("Senha do Funcionário: ");
        String senhaFuncionario = scanner.next();

        if (senhaFuncionario.equals(funcionarioLogado.getSenha())) {
            Relatorio relatorio = new Relatorio();
            // Lógica para geração de relatório
            relatorio.gerarRelatorioGeral(funcionarioLogado.getContas());
            System.out.println("Relatório gerado com sucesso.");
        } else {
            System.out.println("Senha do funcionário inválida.");
        }
    }
}

