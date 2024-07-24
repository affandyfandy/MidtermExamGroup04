package com.midterm.group4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.ProductDto;
import com.midterm.group4.dto.ProductMapper;
import com.midterm.group4.service.ProductService;

import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity addNewProduct(@RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productService.create(product);
        return ResponseEntity.ok("Created successful");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productService.update(id, product);
        return ResponseEntity.ok("Update successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.ok("Delete successful");
    }
    
    
}
