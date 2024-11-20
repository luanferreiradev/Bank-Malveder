package model.impl;

import model.Cliente;
import model.Conta;
import java.time.LocalDate;

public class ContaCorrente extends Conta {
    private Double limite;
    private LocalDate dataDeVencimento;

    public ContaCorrente(Cliente cliente, Long numeroConta, String endereco, String cidade, String tipoConta) {
        super(cliente, numeroConta, endereco, cidade, tipoConta);
        this.limite = 1000.0;
        this.dataDeVencimento = LocalDate.now().plusMonths(1);
    }

    public Double consultarLimite() {
        return limite;
    }

    public LocalDate getDataVencimento() {
        return dataDeVencimento;
    }
}