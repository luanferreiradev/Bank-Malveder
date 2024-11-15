package model.dao.enties;

import java.time.LocalDate;

public abstract class Usuario {

	private Integer id;
	private String nome;
	private LocalDate dataDeNascimento;
	private String telefone;
	
	private Endereco endereco;
	
	private Boolean isLoggedIn; //estado de login
	
	public Usuario() {
	}

	public Usuario(Integer id, String nome, LocalDate dataDeNascimento, String telefone, Integer idEndereco,String cep, String local, Integer numeroCasa, String bairro, String cidade, String estado) {
		super();
		this.setId(id);
		this.nome = nome;
		this.dataDeNascimento = dataDeNascimento;
		this.telefone = telefone;
		this.endereco = new Endereco(idEndereco, cep, local, numeroCasa, bairro, cidade, estado);
		this.isLoggedIn = false; // Inicialmente, o usuário não está logado
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
	
	public Endereco getEndereco() { 
		return endereco; 
	}
	
	public void setEndereco(Integer idEndereco, String cep, String local, Integer numeroCasa, String bairro, String cidade, String estado) { 
		this.endereco = new Endereco(idEndereco, cep, local, numeroCasa, bairro, cidade, estado); 
	}

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public abstract Boolean login(String identificador ,String senha);
	
	public void logout() {
		if (this.isLoggedIn) {
			this.isLoggedIn = false;
			System.out.println("Usuário " + this.nome + " foi deslogado com sucesso.");
		} else {
			System.out.println("Usuário " + this.nome + " já estava deslogado."); 
		}
	}
	
	public abstract String consultarDados();
}
