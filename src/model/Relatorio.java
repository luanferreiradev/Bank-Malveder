package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import model.service.Transacao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class Relatorio {
    private String titulo;
    private List<String> dados;
    private String nomeUsuario;
    private String tipoConta;

    public Relatorio(String titulo, List<String> dados, String nomeUsuario, String tipoConta) {
        this.titulo = titulo;
        this.dados = dados;
        this.nomeUsuario = nomeUsuario;
        this.tipoConta = tipoConta;
    }

    public void gerarRelatorioGeral(Conta conta) {
        List<Transacao> transacoes = conta.consultarExtrato();
        for (Transacao transacao : transacoes) {
            dados.add("Data: " + transacao.getDataHora() + ", Tipo: " + transacao.getTipo() + ", Valor: " + transacao.getValor());
        }
    }

    public void exportarParaExcel(String filePath) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório de Movimentações");

        // Criação do cabeçalho
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Data");
        header.createCell(1).setCellValue("Descrição");
        header.createCell(2).setCellValue("Valor");

        // Preenchimento dos dados
        int rowNum = 1;
        for (String dado : dados) {
            Row row = sheet.createRow(rowNum++);
            String[] partes = dado.split(", ");
            row.createCell(0).setCellValue(partes[0].split(": ")[1]);
            row.createCell(1).setCellValue(partes[1].split(": ")[1]);
            row.createCell(2).setCellValue(Double.parseDouble(partes[2].split(": ")[1]));
        }

        // Criação do arquivo
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Relatório de Movimentações\n");
        sb.append("Nome do Usuário: ").append(nomeUsuario).append("\n");
        sb.append("Tipo de Conta: ").append(tipoConta).append("\n");
        sb.append("Data de Geração: ").append(LocalDateTime.now()).append("\n");
        sb.append("Movimentações:\n");
        for (String dado : dados) {
            sb.append(dado).append("\n");
        }
        return sb.toString();
    }
}