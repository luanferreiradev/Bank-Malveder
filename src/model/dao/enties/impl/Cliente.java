package model.dao.enties.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.dao.enties.Conta;
import model.dao.enties.Usuario;

public class Cliente extends Usuario{

	private Integer idCliente;
	private String senha;
	
	private List<Conta> contas = new ArrayList<>();
	
	public Cliente() {
	}
	
	public Cliente(Integer idCliente, String senha) {
		super();
		this.idCliente = idCliente;
		this.senha = senha;
	}
	

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public Boolean login(String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String consultarDados() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public double consultarSaldo(int numeroConta) {
		for (Conta conta : contas) {
			if (conta.getNumero().equals(numeroConta)) {
				return conta.consultarSaldo();
			}
		}
		throw new IllegalArgumentException("Conta não encontrada!" + numeroConta);
	}
	
	public void depositar(int numeroConta, double valor) {
		for (Conta conta : contas) {
			if (conta.getNumero().equals(numeroConta)) {
				conta.depositar(valor);
				return;
			}
		}
		throw new IllegalArgumentException("Conta não encontrada!" + numeroConta);
	}
	
	public boolean sacar(int numeroConta, double valor) {
		for (Conta conta : contas) {
			if (conta.getNumero().equals(numeroConta)) {
				conta.sacar(valor);
			}
		}
		throw new IllegalArgumentException("Conta não encontrada!" + numeroConta);
	}
	
	/*---------------------------PAREI AQUI-----------------------------------------*/
	public String consultaExtrato() {
		return String.format("teste");
	}
	
	public Double consultarLimite() {
		return 0.0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idCliente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(idCliente, other.idCliente);
	}
}
