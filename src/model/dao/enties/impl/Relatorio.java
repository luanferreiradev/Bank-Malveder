package model.dao.enties.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.dao.enties.Conta;

public class Relatorio {

	private Integer idRelatorio;
	private String tipo;
	private LocalDateTime dataGeracao;
	private List<String> dados = new ArrayList<>();
	
	public Relatorio() {
		this.dataGeracao = LocalDateTime.now();
	}
	
	public Relatorio(Integer idRelatorio, String tipo, LocalDateTime dataGeracao) {
		super();
		this.idRelatorio = idRelatorio;
		this.tipo = tipo;
		this.dataGeracao = LocalDateTime.now();
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
	
	public void gerarRelatorioGeral(List<Conta> contas) {
		dados.clear();
		dados.add("Relatório Geral Gerado em: " + dataGeracao.toString());
		dados.add("Tipo de Relatório: " + tipo);
		dados.add("ID do Relatorio: " + idRelatorio);
		
		double saldoTotal = 0.0;
		
		for (Conta conta : contas) {
			saldoTotal += conta.consultarSaldo();
			dados.add("Conta: " + conta.getNumero());
			/*for (Transacao transacao : conta.getTransacoes()) {
				dados.add(" Transação: " + transacao.getTipo() + " - " + transacao.getValor() + " em " + transacao.getData()); 
			}*/
		}
		dados.add("Saldo Total: " + saldoTotal);
		
		System.out.println("========== Relatório Geral ==========");
		for (String dado : dados) {
			System.out.println(dado);
		}
		System.out.println("=====================================");
	}
}
