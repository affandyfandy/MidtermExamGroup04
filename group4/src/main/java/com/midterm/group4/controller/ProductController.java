package com.midterm.group4.controller;

import java.util.UUID;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.ProductMapper;
import com.midterm.group4.dto.request.CreateProductDTO;
import com.midterm.group4.dto.response.ReadProductDTO;
import com.midterm.group4.exception.InvalidFileContentException;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.ProductService;
import com.midterm.group4.utils.FileUtils;

import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "http://localhost:4200") 
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
    public ResponseEntity<Page<ReadProductDTO>> getAllProduct(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10", required = false) int pageSize,
            @Parameter(description = "Sort order", example = "asc") @RequestParam(defaultValue = "asc", required = false) String sortOrder,
            @Parameter(description = "Sort by field", example = "name") @RequestParam(defaultValue = "name", required = false) String sortBy
    ) {
        Page<Product> pageProduct = productService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        List<ReadProductDTO> listProductDTOs = pageProduct.getContent().stream()
                .map(product -> productMapper.toReadProductDto(product))
                .collect(Collectors.toList());
        Page<ReadProductDTO> pageProductDTOs = new PageImpl<>(listProductDTOs, pageProduct.getPageable(), pageProduct.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(pageProductDTOs);
    }

    @Operation(summary = "Search products", description = "Search products by name and status with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ReadProductDTO>> searchProduct(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10", required = false) int pageSize,
            @Parameter(description = "Sort order", example = "asc") @RequestParam(defaultValue = "asc", required = false) String sortOrder,
            @Parameter(description = "Sort by field", example = "name") @RequestParam(defaultValue = "name", required = false) String sortBy,
            @Parameter(description = "Product name") @RequestParam(required = false) String name,
            @Parameter(description = "Product status") @RequestParam(required = false) String status
    ) {
        Page<Product> pageProduct = productService.findAllByQuery(pageNo, pageSize, sortBy, sortOrder, name, status);
        System.out.println("sprttt by " + sortBy);
        System.out.println("sprtt order " + sortOrder);
        List<ReadProductDTO> listProductDTOs = pageProduct.getContent().stream()
                .map(product -> productMapper.toReadProductDto(product))
                .collect(Collectors.toList());
        Page<ReadProductDTO> pageProductDTOs = new PageImpl<>(listProductDTOs, pageProduct.getPageable(), pageProduct.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(pageProductDTOs);
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the product"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReadProductDTO> getProductById(@Parameter(description = "Product ID") @PathVariable UUID id) throws ObjectNotFoundException {
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toReadProductDto(product));
    }

    @Operation(summary = "Activate product", description = "Activate a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully activated the product"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{id}/activate")
    public ResponseEntity<ReadProductDTO> productActivation(@Parameter(description = "Product ID") @PathVariable UUID id) throws ObjectNotFoundException{
        Product product = productService.updateStatus(id, true);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(product));
    }

    @Operation(summary = "Deactivate product", description = "Deactivate a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully deactivated the product"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ReadProductDTO> productDeactivate(@Parameter(description = "Product ID") @PathVariable UUID id) throws ObjectNotFoundException{
        Product product = productService.updateStatus(id, false);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(product));
    }

    @Operation(summary = "Update product", description = "Update an existing product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully updated the product"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReadProductDTO> updateProduct(@Parameter(description = "Product ID") @PathVariable UUID id, @Parameter(description = "Product DTO") @RequestBody CreateProductDTO dto) throws ObjectNotFoundException{
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
            @ApiResponse(responseCode = "202", description = "Successfully imported products"),
            @ApiResponse(responseCode = "400", description = "Invalid file content"),
            @ApiResponse(responseCode = "500", description = "Error processing the file")
    })
    @PostMapping("/import")
    public ResponseEntity<String> importProduct(@Parameter(description = "Excel file") @RequestParam("file") MultipartFile file) {
        try {
            List<Product> listProduct = FileUtils.readProductFromExcel(file);
            productService.saveAll(listProduct);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Products imported successfully");
        } catch (InvalidFileContentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file content: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file: " + e.getMessage());
        }
    }
}