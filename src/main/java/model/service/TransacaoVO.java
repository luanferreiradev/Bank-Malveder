package model.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Classe que representa uma transação
public class TransacaoVO {
    private String tipoTransacao;
    private BigDecimal valor;
    private LocalDateTime dataHora;
    private String status;

    public TransacaoVO(String tipoTransacao, BigDecimal valor, LocalDateTime dataHora, String status) {
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.dataHora = dataHora;
        this.status = status;
    }

    // Getters e Setters


    public String getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(String tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}