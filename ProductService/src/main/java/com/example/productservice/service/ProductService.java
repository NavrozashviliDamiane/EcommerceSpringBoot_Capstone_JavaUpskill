package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.entity.ImageData;
import com.example.productservice.utils.ImageUtils;
import com.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StorageService storageService;

    public Long createProductWithImage(String name, String description, MultipartFile imageFile) throws IOException {
        ImageData imageData = buildImageDataFromMultipartFile(imageFile);

        Product product = Product.builder()
                .name(name)
                .description(description)
                .imageData(imageData)
                .build();

        return productRepository.save(product).getId();
    }

    public void deleteProductWithImages(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        productOptional.ifPresent(product -> {
            ImageData imageData = product.getImageData();
            if (imageData != null) {
                storageService.delete(imageData);
            }
            productRepository.delete(product);
        });
    }

    public Product updateProductWithImageAndFields(Long productId, String newName, String newDescription, MultipartFile imageFile) throws IOException {
        Product productToUpdate = getProductById(productId);

        if (productToUpdate == null) {
            throw new NoSuchElementException("Product not found: " + productId);
        }

        updateProductDetails(productToUpdate, newName, newDescription);
        updateProductImage(productToUpdate, imageFile);

        return productRepository.save(productToUpdate);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    // Helper methods

    private ImageData buildImageDataFromMultipartFile(MultipartFile imageFile) throws IOException {
        return ImageData.builder()
                .name(imageFile.getOriginalFilename())
                .type(imageFile.getContentType())
                .imageData(ImageUtils.compressImage(imageFile.getBytes()))
                .build();
    }

    private void updateProductDetails(Product productToUpdate, String newName, String newDescription) {
        if (newName != null && !newName.isEmpty()) {
            productToUpdate.setName(newName);
        }
        if (newDescription != null && !newDescription.isEmpty()) {
            productToUpdate.setDescription(newDescription);
        }
    }

    private void updateProductImage(Product productToUpdate, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            ImageData imageData = storageService.saveOrUpdateImage(imageFile);
            productToUpdate.setImageData(imageData);
        }
    }

}
