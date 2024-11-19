package model;

import model.service.Transacao;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class Relatorio {
    private String tipo;
    private LocalDateTime dataGeracao;
    private List<String> dados;
    private String nomeUsuario;
    private String tipoConta;

    public Relatorio(String tipo, List<String> dados, String nomeUsuario, String tipoConta) {
        this.tipo = tipo;
        this.dados = dados;
        this.nomeUsuario = nomeUsuario;
        this.tipoConta = tipoConta;
        this.dataGeracao = LocalDateTime.now();
    }

    public void gerarRelatorioGeral(Conta conta) {
        List<Transacao> extrato = conta.consultarExtrato();
        for (Transacao transacao : extrato) {
            dados.add(transacao.toString());
        }
    }

    public void exportarParaExcel(String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatorio");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Data");
        headerRow.createCell(1).setCellValue("Tipo");
        headerRow.createCell(2).setCellValue("Valor");

        // Fill data rows
        int rowNum = 1;
        for (String dado : dados) {
            Row row = sheet.createRow(rowNum++);
            String[] parts = dado.split(", ");
            row.createCell(0).setCellValue(parts[0].split(": ")[1]);
            row.createCell(1).setCellValue(parts[1].split(": ")[1]);
            row.createCell(2).setCellValue(Double.parseDouble(parts[2].split(": ")[1]));
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // Closing the workbook
        workbook.close();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Relatório de Movimentações\n");
        sb.append("Nome do Usuário: ").append(nomeUsuario).append("\n");
        sb.append("Tipo de Conta: ").append(tipoConta).append("\n");
        sb.append("Data de Geração: ").append(dataGeracao).append("\n");
        sb.append("Movimentações:\n");
        for (String dado : dados) {
            sb.append(dado).append("\n");
        }
        return sb.toString();
    }

    // Getters e Setters
    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public List<String> getDados() {
        return dados;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDados(List<String> dados) {
        this.dados = dados;
    }
}