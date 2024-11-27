package model.enums;

// Enum que representa o status de uma solicitação
public enum StatusSolicitacao {
    PENDENTE(1, "PENDENTE"),
    APROVADO(2, "APROVADO"),
    REJEITADO(3, "REJEITADO");

    private final int id;
    private final String descricao;

    StatusSolicitacao(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusSolicitacao fromId(int id) {
        for (StatusSolicitacao status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status id: " + id);
    }
}