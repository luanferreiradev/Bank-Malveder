package model.impl;

import model.Cliente;
import model.Conta;
import model.enums.Status_Solicitacao;
import model.service.Transacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContaCorrente extends Conta {
    private Double limite;
    private Double divida;
    private LocalDate dataDeVencimento;

    public ContaCorrente(Long idCliente, Cliente cliente, Long numeroConta, String endereco, String cidade, String tipoConta) {
        super(cliente, numeroConta, endereco, cidade, tipoConta);
        this.limite = 1000.0;
        this.divida = 0.0;
        this.dataDeVencimento = LocalDate.now().plusMonths(1);
        setCliente(cliente);
    }

    public Double consultarLimite() {
        return limite;
    }

    public LocalDate getDataVencimento() {
        return dataDeVencimento;
    }

    public void setDataVencimento(LocalDate dataDeVencimento) {
        this.dataDeVencimento = dataDeVencimento;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Double getDivida() {
        return divida;
    }

    public void setDivida(Double divida) {
        this.divida = divida;
    }

    @Override
    public Status_Solicitacao saque(Double valor) {
        if (valor <= saldo + limite) {
            if (valor > saldo) {
                double diferenca = valor - saldo;
                saldo = 0.0;
                divida += diferenca;
                limite -= diferenca;
            } else {
                saldo -= valor;
            }
            adicionarTransacao(new Transacao(LocalDateTime.now(), "Saque", valor));
            return Status_Solicitacao.APROVADO;
        } else {
            return Status_Solicitacao.REPROVADO;
        }
    }

    @Override
    public Double consultaSaldo() {
        return saldo;
    }
}