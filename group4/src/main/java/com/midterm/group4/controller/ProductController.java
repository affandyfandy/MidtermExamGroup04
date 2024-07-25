package com.midterm.group4.controller;

import java.util.UUID;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.ProductDTO;
import com.midterm.group4.dto.ProductMapper;
import com.midterm.group4.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProduct(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
        @RequestParam(defaultValue = "name", required = false) String sortBy
    ) {
        Page<Product> pageProduct = productService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toListDto(pageProduct.getContent()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProduct(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false) String sortOrder,
        @RequestParam(defaultValue = "name", required = false) String sortBy,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "status", required = false) String status
    ) {
        Page<Product> pageProduct = productService.findAllByQuery(pageNo, pageSize, sortOrder, sortBy, name, status);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toListDto(pageProduct.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toDto(product));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ProductDTO> productActivation(@PathVariable UUID id){
        productService.updateStatus(id, true);
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toDto(product));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ProductDTO> productDeactivate(@PathVariable UUID id){
        productService.updateStatus(id, false);
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toDto(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO dto){
        Product product = productMapper.toEntity(dto);
        Product updatedProduct = productService.update(id, product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toDto(updatedProduct));
    }
    
    @PostMapping
    public ResponseEntity<ProductDTO> addNewProduct(@RequestBody ProductDTO productDto) {
        Product product = productMapper.toEntity(productDto);
        Product newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toDto(newProduct));
    }    
}
