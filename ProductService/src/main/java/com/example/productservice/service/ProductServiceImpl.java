package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.exceptions.ProductServiceCustomException;
import com.example.productservice.repository.ProductRepository;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${gcs.bucket.name}")
    private String bucketName;

    private Storage storage;

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateProduct(Product updatedProduct) {
        // Logic to update the product
        // For example:
        Optional<Product> existingProductOptional = productRepository.findById(updatedProduct.getId());

        existingProductOptional.ifPresent(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setQuantity(updatedProduct.getQuantity());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());

            productRepository.save(existingProduct);
        });
    }

    @Override
    public void reduceQuantity(long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product not found"
                ));

        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException(
                    "Product does not have sufficient Quantity"
            );
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

    }


}