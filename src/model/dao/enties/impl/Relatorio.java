package model.dao.enties.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Relatorio {

	private Integer idRelatorio;
	private String tipo;
	private LocalDateTime dataGeracao;
	private List<String> dados = new ArrayList<>();
	
	public Relatorio() {
	}
	
	public Relatorio(Integer idRelatorio, String tipo, LocalDateTime dataGeracao) {
		super();
		this.idRelatorio = idRelatorio;
		this.tipo = tipo;
		this.dataGeracao = dataGeracao;
	}

	public Integer getIdRelatorio() {
		return idRelatorio;
	}

	public void setIdRelatorio(Integer idRelatorio) {
		this.idRelatorio = idRelatorio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getDataGeracao() {
		return dataGeracao;
	}

	public void setDataGeracao(LocalDateTime dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public List<String> getDados() {
		return dados;
	}

	public void setDados(List<String> dados) {
		this.dados = dados;
	}
	
	
}
