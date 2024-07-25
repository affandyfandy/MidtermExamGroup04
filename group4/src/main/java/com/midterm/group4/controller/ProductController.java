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
// import com.midterm.group4.dto.ProductDTO;
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


@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ReadProductDTO>> getAllProduct(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
        @RequestParam(defaultValue = "name", required = false) String sortBy
    ) {
        Page<Product> pageProduct = productService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        List<Product> listProducts = pageProduct.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toListReadProductDto(listProducts));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReadProductDTO>> searchProduct(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false) String sortOrder,
        @RequestParam(defaultValue = "name", required = false) String sortBy,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "status", required = false) String status
    ) {
        Page<Product> pageProduct = productService.findAllByQuery(pageNo, pageSize, sortOrder, sortBy, name, status);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toListReadProductDto(pageProduct.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadProductDTO> getProductById(@PathVariable UUID id) {
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toReadProductDto(product));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ReadProductDTO> productActivation(@PathVariable UUID id){
        productService.updateStatus(id, true);
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(product));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ReadProductDTO> productDeactivate(@PathVariable UUID id){
        productService.updateStatus(id, false);
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadProductDTO> updateProduct(@PathVariable UUID id, @RequestBody CreateProductDTO dto){
        Product product = productMapper.toEntity(dto);
        Product updatedProduct = productService.update(id, product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(updatedProduct));
    }
    
    @PostMapping
    public ResponseEntity<ReadProductDTO> addNewProduct(@RequestBody CreateProductDTO productDto) {
        Product product = productMapper.toEntity(productDto);
        Product newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toReadProductDto(newProduct));
    }    

    @PostMapping("/import")
    public ResponseEntity<List<ReadProductDTO>> importProduct(@RequestParam("file") MultipartFile file) throws IOException {
        List<Product> listProduct = FileUtils.readEmployeeFromExcel(file);
        List<Product> savedListProduct = productService.saveAll(listProduct);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productMapper.toListReadProductDto(savedListProduct));
    }    
}
