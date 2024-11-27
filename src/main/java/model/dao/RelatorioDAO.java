package model.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.service.ExtratoVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;


// @autor: Luan Henrique de Souza Ferreira
public class RelatorioDAO {
    private Connection connection;

    public RelatorioDAO(Connection connection) {
        this.connection = connection;
    }

    public String gerarRelatorioMovimentacao(int idConta) throws SQLException {
        ContaDAO contaDAO = new ContaDAO(connection);
        List<ExtratoVO> extrato = contaDAO.consultarExtrato(idConta);
        StringBuilder conteudo = new StringBuilder();

        if (extrato.isEmpty()) {
            conteudo.append("Nenhuma transação encontrada para a conta.");
        } else {
            ExtratoVO primeiroItem = extrato.get(0);
            conteudo.append("Nome do Dono da Conta: ").append(primeiroItem.getNome()).append("\n");
            conteudo.append("Documento: ").append(primeiroItem.getDocumento()).append("\n");
            conteudo.append("Número da Conta: ").append(primeiroItem.getNumeroConta()).append("\n\n");
            conteudo.append("Transações:\n");

            for (ExtratoVO extratoVO : extrato) {
                conteudo.append("--------------------------------------------------\n");
                conteudo.append("Tipo de Transação: ").append(extratoVO.getTransacao().getTipoTransacao()).append("\n");
                conteudo.append("Valor: ").append(extratoVO.getTransacao().getValor()).append("\n");
                conteudo.append("Data/Hora: ").append(extratoVO.getTransacao().getDataHora()).append("\n");
                conteudo.append("Status: ").append(extratoVO.getTransacao().getStatus()).append("\n");
                conteudo.append("--------------------------------------------------\n");
            }
        }
        System.out.println(conteudo.toString());
        return conteudo.toString();
    }

    public String gerarRelatorioInformacoes(int idUsuario) throws SQLException {
        String sql = "SELECT u.nome, u.documento, u.data_nascimento, u.telefone, u.tipo_usuario, " +
                "e.cep, e.local, e.numero_casa, e.bairro, e.cidade, e.estado, " +
                "c.numero_conta, c.agencia, c.saldo, c.tipo_conta, c.status " +
                "FROM usuario u " +
                "LEFT JOIN endereco e ON u.id_usuario = e.id_usuario " +
                "LEFT JOIN conta c ON u.id_usuario = c.id_cliente " +
                "WHERE u.id_usuario = ?";
        StringBuilder conteudo = new StringBuilder();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    conteudo.append("Nome: ").append(rs.getString("nome")).append("\n");
                    conteudo.append("Documento: ").append(rs.getString("documento")).append("\n");
                    conteudo.append("Data de Nascimento: ").append(rs.getDate("data_nascimento")).append("\n");
                    conteudo.append("Telefone: ").append(rs.getString("telefone")).append("\n");
                    conteudo.append("Tipo de Usuário: ").append(rs.getString("tipo_usuario")).append("\n\n");
                    conteudo.append("Endereço:\n");
                    conteudo.append("CEP: ").append(rs.getString("cep")).append("\n");
                    conteudo.append("Local: ").append(rs.getString("local")).append("\n");
                    conteudo.append("Número da Casa: ").append(rs.getInt("numero_casa")).append("\n");
                    conteudo.append("Bairro: ").append(rs.getString("bairro")).append("\n");
                    conteudo.append("Cidade: ").append(rs.getString("cidade")).append("\n");
                    conteudo.append("Estado: ").append(rs.getString("estado")).append("\n\n");
                    conteudo.append("Conta:\n");
                    conteudo.append("Número da Conta: ").append(rs.getString("numero_conta")).append("\n");
                    conteudo.append("Agência: ").append(rs.getString("agencia")).append("\n");
                    conteudo.append("Saldo: ").append(rs.getDouble("saldo")).append("\n");
                    conteudo.append("Tipo de Conta: ").append(rs.getString("tipo_conta")).append("\n");
                    conteudo.append("Status: ").append(rs.getString("status")).append("\n");
                } else {
                    conteudo.append("Usuário não encontrado.");
                }
            }
        }
        System.out.println(conteudo.toString());
        return conteudo.toString();
    }

    public String gerarRelatorioGeral(int idConta, int idUsuario) throws SQLException {
        StringBuilder conteudo = new StringBuilder();
        conteudo.append("Relatório de Movimentação:\n");
        conteudo.append(gerarRelatorioMovimentacao(idConta)).append("\n");
        conteudo.append("Relatório de Informações:\n");
        conteudo.append(gerarRelatorioInformacoes(idUsuario));
        return conteudo.toString();
    }

    public void exportarRelatorioParaExcel(String conteudo, String caminhoDiretorio) throws IOException {
        String caminhoArquivo = caminhoDiretorio + "/relatorio.xls";
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório");

        String[] linhas = conteudo.split("\n");
        int rowNum = 0;
        for (String linha : linhas) {
            Row row = sheet.createRow(rowNum++);
            String[] colunas = linha.split(": ");
            for (int colNum = 0; colNum < colunas.length; colNum++) {
                Cell cell = row.createCell(colNum);
                cell.setCellValue(colunas[colNum]);
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(caminhoArquivo)) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Relatório exportado para " + caminhoArquivo);
    }
}