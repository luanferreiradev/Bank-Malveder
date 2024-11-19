package model;

import model.impl.ClientePF;
import model.impl.ClientePJ;
import model.impl.ContaCorrente;
import model.service.BancoServico;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Funcionario extends Usuario {
    private String codigoFuncionario;
    private String cargo;
    private BancoServico bancoServico;
    private List<Relatorio> relatorios;

    public Funcionario(String nome, LocalDate dataDeNascimento, String telefone, String senha, String cep, String local, String numeroCasa, String bairro, String cidade, String estado, String codigoFuncionario, String cargo, BancoServico bancoServico) {
        super(nome, dataDeNascimento, telefone, senha, cep, local + ", " + numeroCasa, bairro, cidade, estado);
        this.codigoFuncionario = codigoFuncionario;
        this.cargo = cargo;
        this.bancoServico = bancoServico;
        this.relatorios = new ArrayList<>();
    }

    public void criarConta(String documento, String senha, boolean isPessoaFisica, String endereco, String numeroCasa, String bairro, String cidade, String estado, String dataNascimento, String nome, String telefone, String tipoConta) {
        bancoServico.criarSolicitacao(documento, senha, isPessoaFisica, endereco, numeroCasa, bairro, cidade, estado, dataNascimento, nome, telefone, tipoConta);
    }

    public void removerConta(int numeroConta) {
        Conta conta = bancoServico.getConta(numeroConta);
        if (conta != null) {
            bancoServico.removerConta(conta);
        }
    }

    public String consultarDadosConta(int numeroConta) {
        Conta conta = bancoServico.getConta(numeroConta);
        if (conta != null) {
            return conta.toString();
        }
        return "Conta não encontrada.";
    }

    public String consultarDadosCliente(String documento) {
        Cliente cliente = bancoServico.getCliente(documento);
        if (cliente != null) {
            return cliente.consultarDados();
        } else {
            return "Cliente não encontrado.";
        }
    }

    public void alterarDadosConta(int numeroConta, String novoTipoConta) {
        Conta conta = bancoServico.getConta(numeroConta);
        if (conta != null) {
            conta.alterarTipoConta(novoTipoConta);
        } else {
            System.out.println("Conta não encontrada.");
        }
    }

    // Funcionario.java
    public void alterarDadosCliente(String documento, String novoNome, String novoTelefone, String novaSenha, String novoCep, String novoLocal, String novoBairro, String novaCidade, String novoEstado, LocalDate novaDataNascimento, boolean isPessoaFisica) {
        Cliente cliente = bancoServico.getCliente(documento);
        if (cliente != null) {
            cliente.setNome(novoNome);
            cliente.setTelefone(novoTelefone);
            cliente.setSenha(novaSenha);
            cliente.setDataNascimento(novaDataNascimento.toString());

            Endereco endereco = cliente.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
                cliente.setEndereco(endereco);
            }
            endereco.setCep(novoCep);
            endereco.setLocal(novoLocal);
            endereco.setBairro(novoBairro);
            endereco.setCidade(novaCidade);
            endereco.setEstado(novoEstado);

            // Alterar o tipo de cliente
            // Alterar o tipo de cliente
            if (isPessoaFisica && cliente instanceof ClientePJ) {
                ClientePF novoCliente = new ClientePF(
                        cliente.getDocumento(),
                        cliente.getSenha(),
                        true,
                        endereco.getEnderecoCompleto(),
                        endereco.getNumeroCasa(),
                        endereco.getBairro(),
                        endereco.getCidade(),
                        endereco.getEstado(),
                        cliente.getDataNascimento().toString(),
                        ((ClientePJ) cliente).getCnpj(), // Pass the CNPJ
                        cliente.getNome(),
                        cliente.getTelefone()
                );
                bancoServico.removerCliente(cliente);
                bancoServico.adicionarCliente(novoCliente);
            } else if (!isPessoaFisica && cliente instanceof ClientePF) {
                ClientePJ novoCliente = new ClientePJ(
                        cliente.getDocumento(),
                        cliente.getSenha(),
                        false,
                        endereco.getEnderecoCompleto(),
                        endereco.getNumeroCasa(),
                        endereco.getBairro(),
                        endereco.getCidade(),
                        endereco.getEstado(),
                        cliente.getDataNascimento().toString(),
                        ((ClientePF) cliente).getCpf(), // Pass the CPF
                        cliente.getNome(),
                        cliente.getTelefone()
                );
                bancoServico.removerCliente(cliente);
                bancoServico.adicionarCliente(novoCliente);
            }

            System.out.println("Dados do cliente alterados com sucesso.");
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    public void cadastrarUmFuncionario(String nome, LocalDate dataDeNascimento, String telefone, String senha, String cep, String local, String numeroCasa, String bairro, String cidade, String estado, String codigoFuncionario, String cargo) {
        bancoServico.criarFuncionario(nome, dataDeNascimento, telefone, senha, cep, local, numeroCasa, bairro, cidade, estado, codigoFuncionario, cargo);
    }

    public void gerarRelatorioMovimentacao(int numeroConta) {
        Conta conta = bancoServico.getConta(numeroConta);
        if (conta != null) {
            Cliente cliente = conta.getCliente();
            String nomeUsuario = cliente.getNome();
            String tipoConta = conta instanceof ContaCorrente ? "Corrente" : "Poupança";

            List<String> dados = new ArrayList<>();
            Relatorio relatorio = new Relatorio("Movimentações da Conta", dados, nomeUsuario, tipoConta);
            relatorio.gerarRelatorioGeral(conta);
            relatorios.add(relatorio);
            System.out.println("Relatório de movimentação gerado com sucesso.");
        } else {
            System.out.println("Conta não encontrada.");
        }
    }

    public void exportarRelatorioParaExcel(int index, String filePath) {
        if (index >= 0 && index < relatorios.size()) {
            Relatorio relatorio = relatorios.get(index);
            try {
                relatorio.exportarParaExcel(filePath);
                System.out.println("Relatório exportado com sucesso.");
            } catch (IOException e) {
                System.out.println("Erro ao exportar o relatório: " + e.getMessage());
            }
        } else {
            System.out.println("Índice de relatório inválido.");
        }
    }

    public List<Relatorio> getRelatorios() {
        return relatorios;
    }

    // Getters e Setters para codigoFuncionario e cargo
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



    @Override
    public Boolean login(String senha) {
        return this.getSenha().equals(senha);
    }

    @Override
    public void logout() {

    }

    @Override
    public String consultarDados() {
        return "";
    }
}