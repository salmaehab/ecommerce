package com.eg.swa.ntier.shopping.service;

import java.util.List;
import java.util.Optional;

import com.eg.swa.ntier.shopping.dto.ProductDto;
import org.springframework.stereotype.Service;

import com.eg.swa.ntier.shopping.model.Product;
import com.eg.swa.ntier.shopping.repository.ProductRepository;
import com.eg.swa.ntier.shopping.service.ProductService;

@Service
public class ProductService {
	
	private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Optional<Product> getProductById(Long id)  {
        return productRepository.findById(id);
    }

   public List<Product> searchProductsByName(String name){
        return productRepository.findByNameContainingIgnoreCase(name);
   }

   public Product createProduct(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
       product= productRepository.save(product);
       return product;
   }

    public Product updateProduct(ProductDto productDto){
        Product product = productRepository.findById(productDto.getId()).get();
        if(productDto.getName()!=null)
        {
            product.setName(productDto.getName());
        }
        if(productDto.getPrice()!=null)
        {
            product.setPrice(productDto.getPrice());
        }
        if(productDto.getQuantity()!=null)
        {
            product.setQuantity(productDto.getQuantity());
        }
        product= productRepository.save(product);
        return product;
    }
    public boolean isProductPresent(Long id)
    {
        return productRepository.findById(id).isPresent();
    }

    public void deleteProduct(Long id)
    {
        productRepository.deleteById(id);
    }
}
