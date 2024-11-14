package model.dao.enties.enums;

public enum StatusSolicitacao {

	APROVADO(1),
	REPROVADO(2),
	EM_ANALISE(3);
	
	private int code;
	
	private StatusSolicitacao(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static StatusSolicitacao valueOf(int code) {
		for (StatusSolicitacao value : StatusSolicitacao.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid Status Solicitacao code");
	}
}
