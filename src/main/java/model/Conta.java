package model;

import model.dao.ContaDAO;
import model.service.Transacao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


// Classe Conta
public class Conta {
    private Long id;
    private double saldo;
    private String numeroConta;
    private String agencia;
    private String tipoConta;
    private String status;
    private Long clienteId;
    private List<Transacao> transacoes = new ArrayList<>();

    public Conta() {
    }

    public Conta(Long id, double saldo, String numeroConta, String agencia, String tipoConta, String status, Long clienteId) {
        this.id = id;
        this.saldo = saldo;
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.tipoConta = tipoConta;
        this.status = status;
        this.clienteId = clienteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
        transacoes.add(new Transacao(LocalDateTime.now(), "Depósito", "Crédito", valor));
    }

    public boolean sacar(double valor) {
        if (valor > 0 && this.saldo >= valor) {
            this.saldo -= valor;
            transacoes.add(new Transacao(LocalDateTime.now(), "Saque", "Débito", valor));
            return true;
        }
        return false;
    }

    public double verSaldo() {
        return this.saldo;
    }

    public boolean transferir(Conta destino, double valor) {
        if (this.sacar(valor)) {
            destino.depositar(valor);
            transacoes.add(new Transacao(LocalDateTime.now(), "Transferência", "Débito", valor));
            return true;
        }
        return false;
    }

    public List<Transacao> consultarExtrato() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        return transacoes.stream()
                .filter(t -> t.getDataHora().toLocalDate().isAfter(inicioMes.minusDays(1)))
                .collect(Collectors.toList());
    }

    public void alterarTipoConta(String novoTipo) {
        this.tipoConta = novoTipo;
    }

    public String criarNumeroConta(ContaDAO contaDAO) throws SQLException {
        Random random = new Random();
        String numeroConta;
        boolean existe;

        do {
            int numeroAleatorio = random.nextInt(10000000); // Gera um número aleatório de 7 dígitos
            numeroConta = String.format("%07d", numeroAleatorio) + "0000000"; // Formata para 11 dígitos com os 7 primeiros zeros
            existe = contaDAO.verificarDocumentoCadastrado(numeroConta);
        } while (existe);

        return numeroConta;
    }
}