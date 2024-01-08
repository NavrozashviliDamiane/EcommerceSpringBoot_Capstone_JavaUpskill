package com.example.productservice.service;

import com.example.productservice.entity.Product;
import java.util.List;
import java.util.Optional;


public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    void deleteProduct(Long id);

    void updateProduct(Product updatedProduct);


    void reduceQuantity(long productId, Integer quantity);
}