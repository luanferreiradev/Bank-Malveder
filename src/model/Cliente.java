package model;

import model.enums.Status_Solicitacao;
import model.impl.ContaCorrente;
import model.impl.ContaPoupanca;
import model.service.Transacao;

import java.time.LocalDate;
import java.util.List;

public class Cliente extends Usuario {
    private String documento;
    private String senha;
    private boolean isPessoaFisica;
    private String endereco;
    private String numeroCasa;
    private String bairro;
    private String cidade;
    private String estado;
    private String dataNascimento;
    private Conta conta;
    private String nome;
    private String telefone;

    public Cliente(String documento, String senha, boolean isPessoaFisica, String endereco, String numeroCasa, String bairro, String cidade, String estado, String dataNascimento, String nome, String telefone) {
        this.documento = documento;
        this.senha = senha;
        this.isPessoaFisica = isPessoaFisica;
        this.endereco = endereco;
        this.numeroCasa = numeroCasa;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.dataNascimento = dataNascimento;
        this.nome = nome;
        this.telefone = telefone;
    }


    @Override
    public Boolean login(String senha) {
        return this.getSenha().equals(senha);
    }

    @Override
    public void logout() {
        // Implementar lógica de logout se necessário
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public String getSenha() {
        return senha;
    }

    @Override
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isPessoaFisica() {
        return isPessoaFisica;
    }

    public void setPessoaFisica(boolean pessoaFisica) {
        isPessoaFisica = pessoaFisica;
    }


    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getBairro() {
        return bairro;
    }

    public void serDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getTelefone() {
        return telefone;
    }

    @Override
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Conta getConta() {
        return conta;
    }

    public String getDocumento() {
        return documento;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Double consultarSaldo() {
        return conta.consultaSaldo();
    }

    public Status_Solicitacao depositarValor(Double valor) {
        conta.deposito(valor);
        return Status_Solicitacao.APROVADO;
    }

    public Status_Solicitacao sacar(Double valor) {
        if (valor <= conta.consultaSaldo()) {
            conta.saque(valor);
            return Status_Solicitacao.APROVADO;
        } else {
            return Status_Solicitacao.REPROVADO;
        }
    }

    public List<Transacao> consultarExtrato() {
        return conta.consultarExtrato();
    }

    public Double consultarLimite() {
        return 1000.0; // Exemplo de limite
    }

    public Status_Solicitacao transferir(Conta contaDestino, Double valor) {
        if (valor <= conta.consultaSaldo()) {
            conta.transferencia(contaDestino, valor);
            return Status_Solicitacao.APROVADO;
        } else {
            return Status_Solicitacao.REPROVADO;
        }
    }

    @Override
    public String consultarDados() {
        if (this.conta != null) {
            StringBuilder dados = new StringBuilder();
            dados.append("Nome: ").append(this.getNome()).append("\n")
                    .append("Telefone: ").append(this.getTelefone()).append("\n")
                    .append("Conta: ").append(this.conta.getNumeroConta()).append("\n")
                    .append("Agência: ").append(this.conta.getAgencia()).append("\n")
                    .append("Saldo: ").append(this.conta.consultaSaldo()).append("\n");

            if (this.conta instanceof ContaPoupanca) {
                ContaPoupanca contaPoupanca = (ContaPoupanca) this.conta;
                dados.append("Taxa de Rendimento: ").append(contaPoupanca.getTaxaRendimento()).append("\n");
            } else if (this.conta instanceof ContaCorrente) {
                ContaCorrente contaCorrente = (ContaCorrente) this.conta;
                dados.append("Limite: ").append(contaCorrente.consultarLimite()).append("\n")
                        .append("Data de Vencimento: ").append(contaCorrente.getDataVencimento()).append("\n");
            }

            return dados.toString();
        } else {
            return "Cliente não possui conta associada.";
        }
    }

}