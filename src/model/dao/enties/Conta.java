package model.dao.enties;

import java.util.ArrayList;
import java.util.List;

import model.dao.enties.impl.Cliente;

public abstract class Conta {

	private Integer numero;
	private String agencia;
	private Double saldo = 0.0;
	
	private List<Cliente> clientes = new ArrayList<>();
	
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
	
	public void depositar(double valor) {
		this.saldo += valor;
	}
	
	public boolean sacar(double valor) {
		if  (valor > saldo) {
			return false;
		} else {
			this.saldo -= valor;
			return true;
		}
	}
	
	public double consultarSaldo() {
		return saldo;
	}
}
