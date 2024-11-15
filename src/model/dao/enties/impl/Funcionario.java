package model.dao.enties.impl;

import java.io.Serializable;
import java.util.Objects;

import model.dao.enties.Conta;
import model.dao.enties.Usuario;

public class Funcionario extends Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String codigoFuncionario;
	private String cargo;
	private String senha;
	private String documento;
	
	public Funcionario() {
	}
	
	public Funcionario(String codigoFuncionario, String cargo, String documento) {
		super();
		this.codigoFuncionario = codigoFuncionario;
		this.cargo = cargo;
		this.documento = documento;
	}
	
	public String getCodigoFuncionario() {
		return codigoFuncionario;
	}

	public void setCodigoFuncionario(String codigoFuncionario) {
		this.codigoFuncionario = codigoFuncionario;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@Override
	public Boolean login(String codigoFuncionario, String senha) {
		if (this.codigoFuncionario.equals(codigoFuncionario) && this.senha.equals(senha)) {
			this.setIsLoggedIn(true);
			return true;
		}
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
	
	/*---------------------------PAREI AQUI-----------------------------------------*/
	public void abrirConta(Conta conta) {
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoFuncionario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		return Objects.equals(codigoFuncionario, other.codigoFuncionario);
	}
}
