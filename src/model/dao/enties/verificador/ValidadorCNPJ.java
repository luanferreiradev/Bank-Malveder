package model.dao.enties.verificador;

public class ValidadorCNPJ implements DocumentoVerificador{

	@Override
	public boolean verificar(String cnpj) {
		// TODO Auto-generated method stub
		cnpj = cnpj.replaceAll("\\D", ""); // Remove caracteres não numéricos

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        char dig13, dig14;
        int sm, num, peso, r;

        try {
            // Cálculo do 1º Dígito Verificador
            sm = 0;
            peso = 2;
            for (int i = 11; i >= 0; i--) {
                num = cnpj.charAt(i) - '0';
                sm += num * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }
            r = sm % 11;
            dig13 = (r == 0 || r == 1) ? '0' : (char) ((11 - r) + '0');

            // Cálculo do 2º Dígito Verificador
            sm = 0;
            peso = 2;
            for (int i = 12; i >= 0; i--) {
                num = cnpj.charAt(i) - '0';
                sm += num * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }
            r = sm % 11;
            dig14 = (r == 0 || r == 1) ? '0' : (char) ((11 - r) + '0');

            return dig13 == cnpj.charAt(12) && dig14 == cnpj.charAt(13);
        } catch (Exception e) {
            return false;
        }
	}
}
