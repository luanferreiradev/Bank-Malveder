package controler;

import java.util.Scanner;

public class Bank {
	
	public void limparTela() {
	    try {
	        if (System.getProperty("os.name").contains("Windows")) {
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        } else {
	            System.out.print("\033[H\033[2J");  
	            System.out.flush();
	        }
	    } catch (Exception e) {
	        System.out.println("Erro ao tentar limpar a tela.");
	    }
	}
	
	public Bank() {
		AcessoAoPrograma();
	}
	
	public void AcessoAoPrograma() {
		Scanner sc = new Scanner(System.in);
		System.out.println("======BEM VINDO======");
		
		do {
			limparTela();
			System.out.println("1 - Funcionario");
			System.out.println("2 - Cliente");
			System.out.println("3 - Sair do programa");
			int escolha = sc.nextInt();
			
			switch (escolha) {
				case 1:
					System.out.println("Opção funcionario selecionada.");
					System.out.println("clear");
					break;
				case 2:
					System.out.println("Opção Cliente selecionada.");
					break;
				case 3:
					System.out.println("Saindo do programa...");
					break;
				default:
					System.out.println("Opção invalida. Tente novamente!");
			}
			if (escolha == 3) {
				break;
			}
			
		} while (true);
		sc.close();
	}
}
