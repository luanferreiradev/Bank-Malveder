package model.impl;
import model.Cliente;
import model.Conta;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ContaPoupanca extends Conta {
    private static final Double TAXA_RENDIMENTO = 0.01;
    private Double taxaRendimento;

    public ContaPoupanca(Cliente cliente, Long numeroConta, String endereco, String cidade, String tipoConta) {
        super(cliente, numeroConta, endereco, cidade, tipoConta);
        this.taxaRendimento = TAXA_RENDIMENTO;
    }

    public Double getTaxaRendimento() {
        return taxaRendimento;
    }

    public Double calcularRendimento() {
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataAbertura = super.getMomentoDeAbertura();
        long meses = ChronoUnit.MONTHS.between(dataAbertura, dataAtual);

        if (meses >= 1) {
            return super.consultaSaldo() * TAXA_RENDIMENTO;
        } else {
            System.out.println("Rendimento só pode ser calculado após um mês.");
            return 0.0;
        }
    }
}