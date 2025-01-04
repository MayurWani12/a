

package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Category;
import com.example.entity.Product;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import com.example.service.FileService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("categoryId") Long categoryId,
            @RequestPart("image") MultipartFile imageFile) {

       
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

      
        String imagePath = fileService.saveFile(imageFile);

       
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category); 
        product.setImage(imagePath);   

        
        productRepository.save(product);

        return ResponseEntity.ok(product);
    }


    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
}
