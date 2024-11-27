package model.impl;

import model.Conta;

import java.time.LocalDate;

// Classe ContaCorrente que herda de Conta
public class ContaCorrente extends Conta {
    private double limite;
    private LocalDate dataDeVencimento;
    private double divida;

    public ContaCorrente(Long id, double saldo, String numeroConta, String agencia, String tipoConta, String status, Long clienteId, double limite, LocalDate dataDeVencimento) {
        super(id, saldo, numeroConta, agencia, tipoConta, status, clienteId);
        this.limite = limite;
        this.dataDeVencimento = dataDeVencimento;
        this.divida = 0;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public LocalDate getDataDeVencimento() {
        return dataDeVencimento;
    }

    public void setDataDeVencimento(LocalDate dataDeVencimento) {
        this.dataDeVencimento = dataDeVencimento;
    }

    public double getDivida() {
        return divida;
    }

    public void setDivida(double divida) {
        this.divida = divida;
    }

    @Override
    public boolean sacar(double valor) {
        if (valor > 0 && (getSaldo() + limite) >= valor) {
            if (getSaldo() >= valor) {
                setSaldo(getSaldo() - valor);
            } else {
                double restante = valor - getSaldo();
                setSaldo(0);
                limite -= restante;
                divida += restante;
            }
            return true;
        }
        return false;
    }

    public void pagarDivida(double valor) {
        if (valor > 0 && valor <= divida) {
            divida -= valor;
            limite += valor;
        }
    }

    public double consultarLimite() {
        return limite;
    }
}