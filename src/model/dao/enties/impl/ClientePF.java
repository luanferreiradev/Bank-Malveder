package model.dao.enties.impl;

import model.dao.enties.verificador.DocumentoVerificador;
import model.dao.enties.verificador.ValidadorCPF;

public class ClientePF extends Cliente{
	
	private Integer idClientePF;
	private String cpf;
	
	DocumentoVerificador verificador;
	
	public ClientePF() {
	}
	
	public ClientePF(Integer idClientePF, String cpf) {
		super();
		this.idClientePF = idClientePF;
		this.cpf = cpf;
	}

	public Integer getIdClientePF() {
		return idClientePF;
	}

	public void setIdClientePF(Integer idClientePF) {
		this.idClientePF = idClientePF;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	private boolean isCpfValid(String cpf) {
	    if (cpf == null || cpf.isEmpty()) {
	        throw new IllegalArgumentException("O CPF não pode ser nulo ou vazio.");
	    }

	    ValidadorCPF verificador = new ValidadorCPF();
	    return verificador.verificar(cpf);
	}
	
	@Override
	public Boolean login(String senha, String cpf) {
		if (isCpfValid(cpf) && this.cpf.equals(cpf) && this.getSenha().equals(senha)) {
			this.setIsLoggedIn(true); // Logando o usuário
			return true;
		}
		return false;
	}
}
