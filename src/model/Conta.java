package model;

import model.enums.Status_Solicitacao;
import model.service.Transacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conta {
    private Long numeroConta;
    private String agencia = "777";
    private Double saldo = 0.0;
    private LocalDate dataAbertura = LocalDate.now();
    private Cliente cliente;
    private List<Transacao> transacoes = new ArrayList<>();
    private Double limite = 1000.0;
    private String tipoConta;
    private String endereco;
    private String cidade;

    public Conta(Cliente cliente, Long numeroConta, String endereco, String cidade, String tipoConta) {
        this.cliente = cliente;
        this.numeroConta = numeroConta;
        this.endereco = endereco;
        this.cidade = cidade;
        this.tipoConta = tipoConta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Long getNumeroConta() {
        return numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }


    public void deposito(Double valor) {
        saldo += valor;
        transacoes.add(new Transacao(LocalDateTime.now(), "Depósito", valor));
    }

    public Status_Solicitacao saque(Double valor) {
        if (valor <= saldo + limite) {
            if (valor > saldo) {
                limite -= (valor - saldo);
                saldo = 0.0;
            } else {
                saldo -= valor;
            }
            transacoes.add(new Transacao(LocalDateTime.now(), "Saque", valor));
            return Status_Solicitacao.APROVADO;
        } else {
            return Status_Solicitacao.REPROVADO;
        }
    }

    public Status_Solicitacao transferencia(Conta contaDestino, Double valor) {
        if (valor <= saldo + limite) {
            if (valor > saldo) {
                limite -= (valor - saldo);
                saldo = 0.0;
            } else {
                saldo -= valor;
            }
            contaDestino.deposito(valor);
            transacoes.add(new Transacao(LocalDateTime.now(), "Transferência", valor));
            return Status_Solicitacao.APROVADO;
        } else {
            return Status_Solicitacao.REPROVADO;
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

    public Double consultarLimite() {
        return limite;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public void alterarTipoConta(String novoTipoConta) {
        if (!this.tipoConta.equals(novoTipoConta)) {
            this.tipoConta = novoTipoConta;
            // Implementar lógica adicional para atualizar as configurações da conta com base no novo tipo
            if (novoTipoConta.equals("Corrente")) {
                this.limite = 1000.0; // Exemplo: definir um novo limite para conta corrente
                // Adicionar outras configurações específicas para conta corrente
            } else if (novoTipoConta.equals("Poupanca")) {
                this.limite = 0.0; // Exemplo: definir um novo limite para conta poupança
                // Adicionar outras configurações específicas para conta poupança
            }
            System.out.println("Tipo de conta alterado para: " + novoTipoConta);
        } else {
            System.out.println("A conta já é do tipo: " + novoTipoConta);
        }
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCidade() {
        return cidade;
    }

}