package model.dao.enties;

public class Endereco {

	private Integer idEndereco;
	private String cep;
	private String local;
	private Integer numeroCasa;
	private String bairro;
	private String cidade;
	private String estado;
	
	public Endereco() {
	}
	
	public Endereco(Integer idEndereco, String cep, String local, Integer numeroCasa, String bairro, String cidade, String estado) {
		super();
		this.idEndereco = idEndereco;
		this.cep = cep;
		this.local = local;
		this.numeroCasa = numeroCasa;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
	}

	public Integer getIdEndereco() {
		return idEndereco;
	}

	public void setIdEndereco(Integer idEndereco) {
		this.idEndereco = idEndereco;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Integer getNumeroCasa() {
		return numeroCasa;
	}

	public void setNumeroCasa(Integer numeroCasa) {
		this.numeroCasa = numeroCasa;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Endereco [idEndereco=" + idEndereco + ", cep=" + cep + ", local=" + local + ", numeroCasa=" + numeroCasa
				+ ", bairro=" + bairro + ", cidade=" + cidade + ", estado=" + estado + "]";
	}
}
