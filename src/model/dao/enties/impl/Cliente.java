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
	public Boolean login(String identificador, String senha) {
		// TODO Auto-generated method stub
		return false;
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
	
	public void adicionarConta(Conta conta) {
		this.contas.add(conta);
		conta.adicionarCliente(this);
	}
	
	public List<Conta> getContas(){
		return this.contas;
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
	
	public String consultaExtrato(int numeroConta) {
		for (Conta conta : contas) {
			if (conta.getNumero().equals(numeroConta)) {
				StringBuilder extrato = new StringBuilder();
				extrato.append("Extrato da Conta ").append(numeroConta).append(":\n");
				for (String mov : conta.getMovimentacoes()) {
					extrato.append(mov).append("\n");
				}
				return extrato.toString();
			}
		}
		throw new IllegalArgumentException("Conta não encontrada: " + numeroConta);
	}
	
	public Double consultarLimite(int numeroConta) {
		for (Conta conta : contas) {
			if (conta.getNumero().equals(numeroConta) && conta instanceof ContaCorrente) {
				ContaCorrente contaCorrente = (ContaCorrente) conta;
				return contaCorrente.consultarLimite();
			}
		}
		throw new IllegalArgumentException("Conta não encontrada: " + numeroConta);
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
