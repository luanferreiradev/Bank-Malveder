package model.dao.enties.impl;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.dao.enties.Conta;
import model.dao.enties.Endereco;
import model.dao.enties.Usuario;
import model.dao.enties.enums.StatusSolicitacao;

public class Funcionario extends Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String codigoFuncionario;
	private String cargo;
	private String senha;
	private String documento;
	
	private List<Cliente> clientes; // Lista de clientes acessível pelo funcionário
	
	private List<Funcionario> funcionario;
	
	public Funcionario() {
	}
	
	public Funcionario(String codigoFuncionario, String cargo, String documento) {
		super();
		this.codigoFuncionario = codigoFuncionario;
		this.cargo = cargo;
		this.documento = documento;
		this.funcionario = new ArrayList<>();
	}
	
	public String getCodigoFuncionario() {
		return codigoFuncionario;
	}

	public void setCodigoFuncionario(String codigoFuncionario) {
		this.codigoFuncionario = codigoFuncionario;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public List<Funcionario> getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(List<Funcionario> funcionario) {
		this.funcionario = funcionario;
	}

	@Override
	public Boolean login(String codigoFuncionario, String senha) {
		if (this.codigoFuncionario.equals(codigoFuncionario) && this.senha.equals(senha)) {
			this.setIsLoggedIn(true);
			return true;
		}
		return false;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String consultarDados() {
		return super.toString() + "\n Codigo: " + codigoFuncionario + "\nCargo: " + cargo;
	}
	
	public void abrirConta(Conta conta, Cliente cliente) {
		if (conta != null && cliente != null) {
			cliente.adicionarConta(conta);
			System.out.println("Conta do tipo " + conta.getClass().getSimpleName() + " aberta para o cliente " + cliente.getNome() + ".");
		} else {
			System.out.println("Dados invalidos para abertura de conta.");
		}
	}
	
	public void encerarConta(Conta conta, Cliente cliente, Funcionario funcionario) {
		if (conta != null && cliente != null) {
			List<Conta> contas = cliente.getContas();
			if (contas.remove(conta)) {
				System.out.println("Conta do tipo " + conta.getClass().getSimpleName() + "encerrado para o cliente " + cliente.getNome() + ".");
			} else {
				System.out.println("A conta não foi encontrada para o cliente " + cliente.getNome() + ".");
			}
		}
		else {
			System.out.println("Dados invalidos para encerar a conta.");
		}
	}
	
	public Conta consultarDadosConta(int numeroConta, Cliente cliente) {
		if (cliente != null) {
			for (Conta conta : cliente.getContas()) {
				if (conta.getNumero().equals(numeroConta)) {
					return conta;
				}
			}
			System.out.println("A conta não foi encontrada para o cliente " + cliente.getNome() + ".");
		} else {
			System.out.println("Dados invalidos para consultar a conta.");
		}
		return null;
	}
	
	public Cliente consultarDadosCliente(Integer idCliente) {
		for (Cliente cliente : clientes) {
			if (cliente instanceof ClientePF && ((ClientePF) cliente).getIdClientePF().equals(idCliente)) {
				return cliente;
			} else if(cliente instanceof ClientePJ && ((ClientePJ) cliente).getIdClientePJ().equals(idCliente)) {
				return cliente;
			}
		}
		System.out.println("Cliente não encontrado com ID: " + idCliente);
		return null;
	}
	
	public void alterarDadosDaConta(int numeroConta, Cliente cliente, String novoCampo, Object novoValor) {
		
		Conta conta = consultarDadosConta(numeroConta, cliente);
		if (conta != null) {
			if (conta instanceof ContaPoupanca) {
				ContaPoupanca contaPoupanca = (ContaPoupanca) conta;
				switch (novoCampo) {
					case "taxaDeRendimento":
						contaPoupanca.setTaxaDeRendimento((Double) novoValor);
						break;
					default:
						System.out.println("Campo invalido para ContaPoupanca.");
						return;
				}
				System.out.println("Dados da ContaPoupanca alterados com sucesso.");
			} else if (conta instanceof ContaCorrente) {
				ContaCorrente contaCorrente = (ContaCorrente) conta;
				switch (novoCampo) {
					case "limite":
						contaCorrente.setLimite((Double) novoValor);
						break;
					case "dataVencimento":
						contaCorrente.setDataVencimento((LocalDate) novoValor);
						break;
					default:
						System.out.println("Campo invalido para ContaCorrente.");
						return;
				}
				System.out.println("Dados da conta");
			}
			else {
				System.out.println("Conta não encontrada.");
			}
		}
	}
	
	public void alterarDadosCliente(Integer idCliente, String novoNome, String novoTelefone, String novoEndereco, String nocoDocumento) {
		
		Cliente cliente = consultarDadosCliente(idCliente);
		if (cliente != null) {
			cliente.setNome(novoNome);
			cliente.setTelefone(novoTelefone);
			String[] enderecoPartes = novoEndereco.split(",");
			if (enderecoPartes.length == 6) {
				Endereco endereco = new Endereco();
				endereco.setCep(enderecoPartes[0].trim());
				endereco.setLocal(enderecoPartes[1].trim());
				endereco.setNumeroCasa(Integer.parseInt(enderecoPartes[2].trim()));
				endereco.setBairro(enderecoPartes[3].trim());
				endereco.setCidade(enderecoPartes[4].trim());
				endereco.setEstado(enderecoPartes[5].trim());
				cliente.setEndereco(endereco.getIdEndereco(), endereco.getCep(), endereco.getLocal(), endereco.getNumeroCasa(), endereco.getBairro(), endereco.getCidade(), endereco.getEstado());
			}else {
				System.out.println("Formato de endereço inválido!");
			}
			// Atualização de CPF para ClientePF ou CNPJ para ClientePJ
			
			if (cliente instanceof ClientePF) {
				((ClientePF) cliente).setCpf(nocoDocumento);
				System.out.println("CPF atualizado para o cliente PF com ID " + idCliente + ".");
			} else if (cliente instanceof ClientePJ) {
				((ClientePJ) cliente).setCnpj(nocoDocumento);
				System.out.println("CNPJ atualizado para o cliente PJ com ID " + idCliente + ".");
			}
			System.out.println("Dados do com ID " + idCliente + " foram atualizados.");
		} else {
			System.out.println("Cliente não encontrado com ID: " + idCliente);
		}
	}
	
	public void cadastrarFuncionario(String codigoFuncionario, String cargo, String senha, String documento, Integer idEndereco, String cep, String local, Integer numeroCasa, String bairro, String cidade, String estado) {
	    Funcionario novoFuncionario = new Funcionario(codigoFuncionario, cargo, documento);
	    novoFuncionario.setSenha(senha);
	    novoFuncionario.setEndereco(idEndereco, cep, local, numeroCasa, bairro, cidade, estado);
	    this.funcionario.add(novoFuncionario);
	    System.out.println("Funcionário cadastrado com sucesso: " + codigoFuncionario);
	}
	
	public void gerarRealarioMovimentacao(Relatorio relatorio, List<Conta> contas) {
		relatorio.gerarRelatorioGeral(contas);
	}

	public void aprovarSolicitacao(Conta conta) {
	    if (conta != null) {
	        conta.setStatusSolicitacao(StatusSolicitacao.APROVADO);
	        System.out.println("Conta " + conta.getNumero() + " aprovada.");
	    } else {
	        System.out.println("Conta inválida para aprovação.");
	    }
	}

	public void reprovarSolicitacao(Conta conta) {
	    if (conta != null) {
	        conta.setStatusSolicitacao(StatusSolicitacao.REPROVADO);
	        System.out.println("Conta " + conta.getNumero() + " reprovada.");
	    } else {
	        System.out.println("Conta inválida para reprovação.");
	    }
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(codigoFuncionario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		return Objects.equals(codigoFuncionario, other.codigoFuncionario);
	}
}
