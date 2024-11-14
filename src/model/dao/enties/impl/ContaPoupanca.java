package model.dao.enties.impl;

import java.io.Serializable;
import java.util.Objects;

import model.dao.enties.Conta;

public class ContaPoupanca extends Conta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer idContaPoupanca;
	private Double taxaDeRendimento;
	
	public ContaPoupanca() {
	}

	public ContaPoupanca(Integer idContaPoupanca, Double taxaDeRendimento) {
		super();
		this.idContaPoupanca = idContaPoupanca;
		this.taxaDeRendimento = taxaDeRendimento;
	}
	
	public Integer getIdContaPoupanca() {
		return idContaPoupanca;
	}

	public void setIdContaPoupanca(Integer idContaPoupanca) {
		this.idContaPoupanca = idContaPoupanca;
	}

	public Double getTaxaDeRendimento() {
		return taxaDeRendimento;
	}

	public void setTaxaDeRendimento(Double taxaDeRendimento) {
		this.taxaDeRendimento = taxaDeRendimento;
	}
	
	public Double calcularRendimento() {
		double taxaMensal = taxaDeRendimento / 12.0;
		return consultarSaldo() * taxaMensal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idContaPoupanca);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaPoupanca other = (ContaPoupanca) obj;
		return Objects.equals(idContaPoupanca, other.idContaPoupanca);
	}
}
