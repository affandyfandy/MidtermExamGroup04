package com.midterm.group4.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.request.CreateProductDTO;

public class FileUtils {

    public static List<Product> readEmployeeFromExcel(MultipartFile file) throws IOException {
        List<Product> listProduct = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())){
    
            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0); 

            for (Row row : sheet) {

                // Skip header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Read data from each cell
                String name = row.getCell(0).getStringCellValue();
                double price = row.getCell(1).getNumericCellValue();
                double quantity = row.getCell(2).getNumericCellValue();
                boolean isActive = row.getCell(3).getBooleanCellValue();

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
}