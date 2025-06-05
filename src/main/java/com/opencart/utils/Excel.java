package com.opencart.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Excel {

    public static List<Map<String, String>> readSheet(String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (InputStream is = Excel.class.getClassLoader().getResourceAsStream(Constants.INPUT_EXCEL);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            int columnCount = headerRow.getPhysicalNumberOfCells();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < columnCount; j++) {
                    Cell headerCell = headerRow.getCell(j);
                    if (headerCell == null) {
                        continue;
                    }

                    String header = headerCell.getStringCellValue().trim();
                    Cell valueCell = row.getCell(j);
                    String value = getCellValueAsString(valueCell);
                    rowData.put(header, value);
                }
                data.add(rowData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public static void writeProductsToExcel(String filePath, List<String> products) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ProductosAgregados");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Producto");

        for (int i = 0; i < products.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(products.get(i));
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
