package model.service;

import java.time.LocalDateTime;

// Classe que representa uma transação
public class Transacao {
    private LocalDateTime dataHora;
    private String tipo;
    private Double valor;
    private String descricao;

    public Transacao(LocalDateTime dataHora, String descricao, String tipo, Double valor) {
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getTipo() {
        return tipo;
    }

    public Double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Data: " + dataHora + ", Tipo: " + tipo + ", Valor: " + valor + ", Descrição: " + descricao;
    }
}