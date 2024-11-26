package model.impl;

import model.Conta;

// Classe ContaPoupanca que herda de Conta
public class ContaPoupanca extends Conta {
    private double taxaDeRendimento;

    public ContaPoupanca(Long id, double saldo, String numeroConta, String agencia, String tipoConta, String status, Long clienteId, double taxaDeRendimento) {
        super(id, saldo, numeroConta, agencia, tipoConta, status, clienteId);
        this.taxaDeRendimento = taxaDeRendimento;
    }

    public double getTaxaDeRendimento() {
        return taxaDeRendimento;
    }

    public void setTaxaDeRendimento(double taxaDeRendimento) {
        this.taxaDeRendimento = taxaDeRendimento;
    }

    public void calcularRendimentoMensal() {
        double rendimento = getSaldo() * (taxaDeRendimento / 100);
        depositar(rendimento);
    }
}