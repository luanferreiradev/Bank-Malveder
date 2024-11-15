package model.dao.enties.verificador;

public class ValidadorCPF implements DocumentoVerificador{

	@Override
	public boolean verificar(String cpf) {
		// TODO Auto-generated method stub
		cpf = cpf.replaceAll("\\D", ""); // Remove caracteres não numéricos

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sm, num, peso, r;
        char dig10, dig11;

        try {
            // Cálculo do 1º Dígito Verificador
            sm = 0;
            peso = 10;
            for (int i = 0; i < 9; i++) {
                num = cpf.charAt(i) - '0';
                sm += num * peso--;
            }
            r = 11 - (sm % 11);
            dig10 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

            // Cálculo do 2º Dígito Verificador
            sm = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                num = cpf.charAt(i) - '0';
                sm += num * peso--;
            }
            r = 11 - (sm % 11);
            dig11 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

            return dig10 == cpf.charAt(9) && dig11 == cpf.charAt(10);
        } catch (Exception e) {
            return false;
        }
    }
}
