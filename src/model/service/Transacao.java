package model.service;


import java.time.LocalDateTime;

public class Transacao {
    private LocalDateTime dataHora;
    private String tipo;
    private Double valor;

    public Transacao(LocalDateTime dataHora, String tipo, Double valor) {
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.valor = valor;
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
        return "Data: " + dataHora + ", Tipo: " + tipo + ", Valor: " + valor;
    }
}
