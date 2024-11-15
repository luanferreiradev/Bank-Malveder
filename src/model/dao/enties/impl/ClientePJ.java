package model.dao.enties.impl;

import model.dao.enties.verificador.DocumentoVerificador;
import model.dao.enties.verificador.ValidadorCNPJ;

public class ClientePJ extends Cliente{

	private Integer idClientePJ;
	private String cnpj;
	
	DocumentoVerificador verificador;
	
	public ClientePJ() {
	}

	public ClientePJ(Integer idClientePJ, String cnpj) {
		super();
		this.idClientePJ = idClientePJ;
		this.cnpj = cnpj;
	}

	public Integer getIdClientePJ() {
		return idClientePJ;
	}

	public void setIdClientePJ(Integer idClientePJ) {
		this.idClientePJ = idClientePJ;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	private boolean isCnpjValid(String cnpj) {
	    if (cnpj == null || cnpj.isEmpty()) {
	        throw new IllegalArgumentException("O CPF não pode ser nulo ou vazio.");
	    }

	    ValidadorCNPJ verificador = new ValidadorCNPJ();
	    return verificador.verificar(cnpj);
	}
	
	@Override
	public Boolean login(String senha, String cnpj) {
		if (isCnpjValid(cnpj) && this.cnpj.equals(cnpj) && this.getSenha().equals(senha)) {
			this.setIsLoggedIn(true); // Logando o usuário
			return true;
		}
		return false;
	}
	
	@Override
	public String consultarDados() {
		return super.toString() + "\nCNPJ: " + cnpj;
	}
}
