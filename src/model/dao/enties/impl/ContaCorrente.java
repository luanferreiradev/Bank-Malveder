package model.dao.enties.impl;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import model.dao.enties.Conta;

public class ContaCorrente extends Conta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer idContaCorrente;
	private Double limite;
	private LocalDate dataVencimento;
	
	public ContaCorrente() {
	}

	public ContaCorrente(Integer idContaCorrente, Double limite, LocalDate dataVencimento) {
		super();
		this.idContaCorrente = idContaCorrente;
		this.limite = limite;
		this.dataVencimento = dataVencimento;
	}

	public Integer getIdContaCorrente() {
		return idContaCorrente;
	}

	public void setIdContaCorrente(Integer idContaCorrente) {
		this.idContaCorrente = idContaCorrente;
	}

	public Double getLimite() {
		return limite;
	}

	public void setLimite(Double limite) {
		this.limite = limite;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public Double consultarLimite() {
		return consultarSaldo() + getLimite();
	}

	@Override
	public int hashCode() {
		return Objects.hash(idContaCorrente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaCorrente other = (ContaCorrente) obj;
		return Objects.equals(idContaCorrente, other.idContaCorrente);
	}
}
