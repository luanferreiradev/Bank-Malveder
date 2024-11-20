package model.impl;
import model.Cliente;
import model.Conta;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ContaPoupanca extends Conta {
    private Double taxaRendimento;

    public ContaPoupanca(Cliente cliente, Long numeroConta, String endereco, String cidade, String tipoConta) {
        super(cliente, numeroConta, endereco, cidade, tipoConta);
        this.taxaRendimento = 0.10;
    }

    public Double getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(Double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }

    @Override
    public Double consultaSaldo() {
        calcularRendimento();
        return super.consultaSaldo();
    }

    public Double calcularRendimento() {
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataAbertura = super.getMomentoDeAbertura();
        long meses = ChronoUnit.MONTHS.between(dataAbertura, dataAtual);

        if (meses >= 1) {
            Double rendimento = super.consultaSaldo() * (taxaRendimento / 12);
            super.deposito(rendimento);
            return rendimento;
        } else {
            return 0.0;
        }
    }
}