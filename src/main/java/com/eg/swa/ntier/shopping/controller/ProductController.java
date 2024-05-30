package com.eg.swa.ntier.shopping.controller;

import java.util.List;
import java.util.Optional;

import com.eg.swa.ntier.shopping.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eg.swa.ntier.shopping.model.Product;
import com.eg.swa.ntier.shopping.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws Exception {
        Optional<Product> product = productService.getProductById(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    

    @GetMapping("/search")
    public  ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name){
        List<Product> products = productService
                .searchProductsByName(name);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    
    // Need have a function to create product
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
        System.out.print("pdto"+productDto);
        Product product= productService.createProduct(productDto);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    // Need have a function to update product
    @PostMapping("/update")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productDto) {
        if(!productService.isProductPresent(productDto.getId()))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Product product = productService.updateProduct(productDto);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }
    
   // Need have a function to delete product
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id)
    {
        if(!productService.isProductPresent(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

   
}

