package model;

import model.enums.Status_Solicitacao;
import model.impl.ContaCorrente;
import model.impl.ContaPoupanca;
import model.service.Transacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conta {
    protected Double saldo = 0.0;
    private Long numeroConta;
    private String agencia = "777";
    private LocalDate dataAbertura = LocalDate.now();
    private Cliente cliente;
    private List<Transacao> transacoes = new ArrayList<>();
    private String tipoConta;
    private String endereco;
    private String cidade;
    private Status_Solicitacao status;

    public Conta(Cliente cliente, Long numeroConta, String endereco, String cidade, String tipoConta) {
        this.cliente = cliente;
        this.numeroConta = numeroConta;
        this.endereco = endereco;
        this.cidade = cidade;
        this.tipoConta = tipoConta;
        this.status = Status_Solicitacao.EM_ANALISE; // Inicializa o status como EM_ANALISE
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public Long getNumeroConta() {
        return numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    protected void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public void deposito(Double valor) {
        saldo += valor;
        transacoes.add(new Transacao(LocalDateTime.now(), "Depósito", valor));
    }

    public Status_Solicitacao saque(Double valor) {
        if (valor <= saldo) {
            saldo -= valor;
            transacoes.add(new Transacao(LocalDateTime.now(), "Saque", valor));
            return Status_Solicitacao.APROVADO;
        } else {
            return Status_Solicitacao.REPROVADO;
        }
    }

    public Status_Solicitacao transferencia(Conta contaDestino, Double valor) {
        if (valor <= saldo) {
            saldo -= valor;
            contaDestino.deposito(valor);
            transacoes.add(new Transacao(LocalDateTime.now(), "Transferência", valor));
            return Status_Solicitacao.APROVADO;
        } else {
            return Status_Solicitacao.REPROVADO;
        }
    }

    public void alterarTipoConta(String novoTipoConta) {
        if (!this.tipoConta.equals(novoTipoConta)) {
            if (novoTipoConta.equals("Corrente")) {
                ContaCorrente novaConta = new ContaCorrente(
                        this.cliente.getId(), // Adiciona o idCliente
                        this.cliente,
                        this.numeroConta,
                        this.endereco,
                        this.cidade,
                        novoTipoConta
                );
                novaConta.setLimite(1000.0); // Definir limite para conta corrente
                novaConta.setStatus(Status_Solicitacao.APROVADO); // Aprovar a nova conta
                // Adicionar outras configurações específicas para conta corrente
            } else if (novoTipoConta.equals("Poupanca")) {
                ContaPoupanca novaConta = new ContaPoupanca(
                        this.cliente.getId(), // Adiciona o idCliente
                        this.cliente,
                        this.numeroConta,
                        this.endereco,
                        this.cidade,
                        novoTipoConta
                );
                novaConta.setStatus(Status_Solicitacao.APROVADO); // Aprovar a nova conta
                // Adicionar outras configurações específicas para conta poupança
            }
            this.tipoConta = novoTipoConta;
            System.out.println("Tipo de conta alterado para: " + novoTipoConta);
        } else {
            System.out.println("A conta já é do tipo: " + novoTipoConta);
        }
    }

    public Double consultaSaldo() {
        return saldo;
    }

    public LocalDate getMomentoDeAbertura() {
        return dataAbertura;
    }

    public List<Transacao> consultarExtrato() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        return transacoes.stream()
                .filter(t -> t.getDataHora().toLocalDate().isAfter(inicioMes.minusDays(1)))
                .collect(Collectors.toList());
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Status_Solicitacao getStatus() {
        return this.status;
    }

    public void setStatus(Status_Solicitacao status) {
        this.status = status;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getCidade() {
        return cidade;
    }

    protected void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}