package com.midterm.group4.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.exception.InvalidFileContentException;

public class FileUtils {

    public static List<Product> readProductFromExcel(MultipartFile file) throws IOException {
        List<Product> listProduct = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); 
            Iterator<Row> rowIterator = sheet.iterator();
            
            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            int rowIndex = 1; // Start counting rows from 1 (after header)
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowIndex++;
                
                if (isEmptyRow(row)) {
                    continue;
                }

                String name;
                double price;
                double quantity;
                boolean isActive;

                try {
                    name = getCellValueAsString(row, 0);
                    price = getCellValueAsDouble(row, 1);
                    quantity = getCellValueAsDouble(row, 2);
                    isActive = getCellValueAsBoolean(row, 3);
                } catch (Exception e) {
                    throw new InvalidFileContentException("Error reading data from row " + rowIndex, e);
                }

                if (name == null || name.trim().isEmpty() || price < 0 || quantity < 0){
                    break;
                }

                int quantityInt = (int) Math.round(quantity);

                // Create Product object and add to list
                Product productDTO = new Product();
                productDTO.setName(name);
                productDTO.setActive(isActive);
                productDTO.setPrice(BigInteger.valueOf((long) price));
                productDTO.setQuantity(quantityInt);
                listProduct.add(productDTO);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error reading Excel file", e);
        }

        return listProduct;
    }

    private static boolean isEmptyRow(Row row) {
        return row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null || row.getCell(3) == null;
    }

    private static String getCellValueAsString(Row row, int cellIndex) {
        if (row.getCell(cellIndex) != null) {
            return row.getCell(cellIndex).getStringCellValue();
        }
        return "";
    }

    private static double getCellValueAsDouble(Row row, int cellIndex) {
        if (row.getCell(cellIndex) != null) {
            return row.getCell(cellIndex).getNumericCellValue();
        }
        return 0.0;
    }

    private static boolean getCellValueAsBoolean(Row row, int cellIndex) {
        if (row.getCell(cellIndex) != null) {
            return row.getCell(cellIndex).getBooleanCellValue();
        }
        return false;
    }
}
