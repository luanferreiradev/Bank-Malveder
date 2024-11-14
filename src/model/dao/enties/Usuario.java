package model.dao.enties;

import java.time.LocalDate;

public abstract class Usuario {

	private Integer id;
	private String nome;
	private LocalDate dataDeNascimento;
	private String telefone;
	
	public Usuario() {
	}

	public Usuario(Integer id, String nome, LocalDate dataDeNascimento, String telefone) {
		super();
		this.setId(id);
		this.nome = nome;
		this.dataDeNascimento = dataDeNascimento;
		this.telefone = telefone;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataDeNascimento() {
		return dataDeNascimento;
	}

	public void setDataDeNascimento(LocalDate dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public abstract Boolean login(String senha);
	
	public abstract void logout();
	
	public abstract String consultarDados();
}
