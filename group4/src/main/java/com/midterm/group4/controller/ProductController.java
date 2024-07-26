package com.midterm.group4.controller;

import java.util.UUID;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.ProductMapper;
import com.midterm.group4.dto.request.CreateProductDTO;
import com.midterm.group4.dto.response.ReadProductDTO;
import com.midterm.group4.service.ProductService;
import com.midterm.group4.utils.FileUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/product")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Operation(summary = "Get all products", description = "Retrieve all products with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all products")
    })
    @GetMapping
    public ResponseEntity<List<ReadProductDTO>> getAllProduct(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10", required = false) int pageSize,
            @Parameter(description = "Sort order", example = "asc") @RequestParam(defaultValue = "asc", required = false) String sortOrder,
            @Parameter(description = "Sort by field", example = "name") @RequestParam(defaultValue = "name", required = false) String sortBy
    ) {
        Page<Product> pageProduct = productService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        List<Product> listProducts = pageProduct.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toListReadProductDto(listProducts));
    }

    @Operation(summary = "Search products", description = "Search products by name and status with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ReadProductDTO>> searchProduct(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10", required = false) int pageSize,
            @Parameter(description = "Sort order", example = "asc") @RequestParam(defaultValue = "asc", required = false) String sortOrder,
            @Parameter(description = "Sort by field", example = "name") @RequestParam(defaultValue = "name", required = false) String sortBy,
            @Parameter(description = "Product name") @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "Product status") @RequestParam(value = "status", required = false) String status
    ) {
        Page<Product> pageProduct = productService.findAllByQuery(pageNo, pageSize, sortOrder, sortBy, name, status);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toListReadProductDto(pageProduct.getContent()));
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the product")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReadProductDTO> getProductById(@Parameter(description = "Product ID") @PathVariable UUID id) {
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toReadProductDto(product));
    }

    @Operation(summary = "Activate product", description = "Activate a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully activated the product")
    })
    @PostMapping("/{id}/activate")
    public ResponseEntity<ReadProductDTO> productActivation(@Parameter(description = "Product ID") @PathVariable UUID id){
        productService.updateStatus(id, true);
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(product));
    }

    @Operation(summary = "Deactivate product", description = "Deactivate a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully deactivated the product")
    })
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ReadProductDTO> productDeactivate(@Parameter(description = "Product ID") @PathVariable UUID id){
        productService.updateStatus(id, false);
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(product));
    }

    @Operation(summary = "Update product", description = "Update an existing product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully updated the product")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReadProductDTO> updateProduct(@Parameter(description = "Product ID") @PathVariable UUID id, @Parameter(description = "Product DTO") @RequestBody CreateProductDTO dto){
        Product product = productMapper.toEntity(dto);
        Product updatedProduct = productService.update(id, product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(updatedProduct));
    }

    @Operation(summary = "Create new product", description = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully created the product")
    })
    @PostMapping
    public ResponseEntity<ReadProductDTO> addNewProduct(@Parameter(description = "Product DTO") @RequestBody CreateProductDTO productDto) {
        Product product = productMapper.toEntity(productDto);
        Product newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(newProduct));
    }

    @Operation(summary = "Import products from Excel", description = "Import products from an Excel file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully imported products")
    })
    @PostMapping("/import")
    public ResponseEntity<List<ReadProductDTO>> importProduct(@Parameter(description = "Excel file") @RequestParam("file") MultipartFile file) throws IOException {
        List<Product> listProduct = FileUtils.readEmployeeFromExcel(file);
        List<Product> savedListProduct = productService.saveAll(listProduct);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toListReadProductDto(savedListProduct));
    }
}
