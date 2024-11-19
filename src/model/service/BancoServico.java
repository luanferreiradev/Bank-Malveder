package model.service;

import model.*;
import model.enums.Status_Solicitacao;
import model.impl.ClientePF;
import model.impl.ClientePJ;
import model.impl.ContaCorrente;
import model.impl.ContaPoupanca;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static model.Banco.*;

public class BancoServico extends Usuario {
    private static int nextCodigoFuncionario = 2;
    private Map<Long, Conta> contas = new HashMap<>();
    private Map<String, Long> documentoParaId = new HashMap<>();
    private Map<String, Cliente> clientes = new HashMap<>();
    private Map<Integer, Solicitacao> solicitacoes = new HashMap<>();
    private int proximoIdSolicitacao = 1;
    private Map<String, Funcionario> funcionarios = new HashMap<>();
    private Random random = new Random();
    private boolean isLoggedIn = false;

    public BancoServico(String nome, LocalDate dataDeNascimento, String telefone, String senha, String cep, String local, String numeroCasa, String bairro, String cidade, String estado) {
        super(nome, dataDeNascimento, telefone, senha, cep, local + ", " + numeroCasa, bairro, cidade, estado);
        this.funcionarios = new HashMap<>();
    }

    // BancoServico.java
    // BancoServico.java
    public void criarSolicitacao(String documento, String senha, boolean isPessoaFisica, String endereco, String numeroCasa, String bairro, String cidade, String estado, String dataNascimento, String nome, String telefone, String tipoConta) {
        Long numeroConta = gerarNumeroConta(); // Gerar número de conta no momento da criação
        Solicitacao solicitacao = new Solicitacao(proximoIdSolicitacao++, documento, senha, tipoConta, endereco, numeroCasa, bairro, cidade, estado, dataNascimento, Status_Solicitacao.EM_ANALISE);
        solicitacao.setNumeroConta(numeroConta); // Atribuir número de conta
        solicitacoes.put(solicitacao.getId(), solicitacao);

        // Criar cliente no momento da criação da solicitação
        Cliente cliente;
        String numeroContaStr = numeroConta.toString(); // Converter Long para String
        if (isPessoaFisica) {
            cliente = new ClientePF(documento, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, dataNascimento, numeroContaStr, nome, telefone);
        } else {
            cliente = new ClientePJ(documento, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, dataNascimento, numeroContaStr, nome, telefone);
        }
        clientes.put(documento, cliente);
    }

    public Solicitacao getSolicitacao(int idSolicitacao) {
        return solicitacoes.values().stream()
                .filter(s -> s.getId() == idSolicitacao)
                .findFirst()
                .orElse(null);
    }

    // BancoServico.java
    // BancoServico.java
    public void aprovarSolicitacao(int idSolicitacao) {
        Solicitacao solicitacao = solicitacoes.get(idSolicitacao);
        if (solicitacao == null) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }

        Cliente cliente = clientes.get(solicitacao.getDocumento());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        // Aprovar a solicitação e criar a conta
        Conta conta;
        if (solicitacao.getTipoConta().equals("Corrente")) {
            conta = new ContaCorrente(cliente, solicitacao.getNumeroConta(), solicitacao.getEndereco(), solicitacao.getCidade(), solicitacao.getTipoConta());
        } else {
            conta = new ContaPoupanca(cliente, solicitacao.getNumeroConta(), solicitacao.getEndereco(), solicitacao.getCidade(), solicitacao.getTipoConta());
        }

        adicionarConta(conta); // Certifique-se de que este método está acessível
        solicitacao.setStatus(Status_Solicitacao.APROVADO);
    }

    public void adicionarConta(Conta conta) {
        contas.put(conta.getNumeroConta(), conta);
    }

    public void rejeitarSolicitacao(int idSolicitacao) {
        Solicitacao solicitacao = solicitacoes.values().stream()
                .filter(s -> s.getId() == idSolicitacao)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
        solicitacao.setStatus(Status_Solicitacao.REPROVADO);
    }

    public List<Solicitacao> listarSolicitacoes() {
        return new ArrayList<>(solicitacoes.values());
    }

    public void alterarTipoConta(Conta conta, String novoTipoConta) {
        if (novoTipoConta.equals("Corrente")) {
            ContaCorrente novaConta = new ContaCorrente(conta.getCliente(), conta.getNumeroConta(), conta.getEndereco(), conta.getCidade(), novoTipoConta);
            contas.put(conta.getNumeroConta(), novaConta);
        } else if (novoTipoConta.equals("Poupança")) {
            ContaPoupanca novaConta = new ContaPoupanca(conta.getCliente(), conta.getNumeroConta(), conta.getEndereco(), conta.getCidade(), novoTipoConta);
            contas.put(conta.getNumeroConta(), novaConta);
        }
    }

    private Long gerarNumeroConta() {
        long numeroConta;
        do {
            numeroConta = Math.abs(random.nextLong() % 10000000L); // Gera um número de até 7 dígitos
        } while (contaExiste(numeroConta));
        return Long.parseLong(String.format("%07d%04d", 0, numeroConta)); // Formata para 11 dígitos com os 7 primeiros sendo zeros
    }

    // BancoServico.java
    public void adicionarCliente(Cliente cliente) {
        clientes.put(cliente.getDocumento(), cliente);
    }

    // BancoServico.java
    public void removerCliente(Cliente cliente) {
        clientes.remove(cliente.getDocumento());
    }

    private boolean contaExiste(Long numeroConta) {
        return contas.values().stream().anyMatch(conta -> conta.getNumeroConta().equals(numeroConta.intValue()));
    }

    private Long gerarIdUnico() {
        return Math.abs(random.nextLong());
    }

    public Conta getConta(int numeroConta) {
        return contas.get((long) numeroConta);
    }

    public void removerConta(Conta conta) {
        contas.values().removeIf(c -> c.equals(conta));
    }

    public Cliente getCliente(String documento) {
        return clientes.get(documento);
    }

    public void adicionarFuncionario(Funcionario funcionario) {
        funcionarios.put(funcionario.getCodigoFuncionario(), funcionario);
    }

    public Funcionario getFuncionario(String codigoFuncionario) {
        for (Funcionario funcionario : funcionarios.values()) {
            if (funcionario.getCodigoFuncionario().equals(codigoFuncionario)) {
                return funcionario;
            }
        }
        return null;
    }

    public static String gerarProximoCodigoFuncionario() {
        return String.format("%04d", nextCodigoFuncionario++);
    }

    public void criarFuncionario(String nome, LocalDate dataDeNascimento, String telefone, String senha, String cep, String local, String numeroCasa, String bairro, String cidade, String estado, String codigoFuncionario, String cargo) {
        Funcionario novoFuncionario = new Funcionario(nome, dataDeNascimento, telefone, senha, cep, local, numeroCasa, bairro, cidade, estado, codigoFuncionario, cargo, this);
        funcionarios.put(codigoFuncionario, novoFuncionario);
    }

    @Override
    public Boolean login(String senha) {
        if (this.getSenha().equals(senha)) {
            this.isLoggedIn = true;
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        this.isLoggedIn = false;
    }

    @Override
    public String consultarDados() {
        return this.toString();
    }
}