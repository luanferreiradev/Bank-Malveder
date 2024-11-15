package model.dao.enties;

import java.util.ArrayList;
import java.util.List;

import model.dao.enties.enums.StatusSolicitacao;
import model.dao.enties.impl.Cliente;

public abstract class Conta {

	private Integer numero;
	private String agencia;
	private Double saldo = 0.0;
	
	private List<String> movimentacoes = new ArrayList<>();
	private List<Cliente> clientes = new ArrayList<>();
	
	private StatusSolicitacao statusSolicitacao = StatusSolicitacao.EM_ANALISE;
	
	public Conta() {
	}

	public Conta(Integer numero, String agencia) {
		super();
		this.numero = numero;
		this.agencia = agencia;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	
	public StatusSolicitacao getStatusSolicitacao() {
		return statusSolicitacao;
	}

	public void setStatusSolicitacao(StatusSolicitacao statusSolicitacao) {
		this.statusSolicitacao = statusSolicitacao;
	}

	public void depositar(double valor) {
		this.saldo += valor;
		movimentacoes.add("Deposito: " + valor);
	}
	
	public boolean sacar(double valor) {
		if  (valor > saldo) {
			return false;
		} else {
			this.saldo -= valor;
			movimentacoes.add("Saque: " + valor);
			return true;
		}
	}
	
	public double consultarSaldo() {
		return saldo;
	}
	
	public void adicionarCliente(Cliente cliente) {
		this.clientes.add(cliente);
	}
	
	public List<Cliente> getClientes(){
		return this.clientes;
	}
	
	public List<String> getMovimentacoes(){
		return movimentacoes;
	}
}
