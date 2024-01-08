package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import com.example.productservice.service.GoogleStorageService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/product")
public class ProductController {

    @Value("${gcs.bucket.name}")
    private String bucketName;

    private final Storage storage;
    private final ProductService productService;
    private final ProductRepository productRepository;


    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) throws IOException {
        this.productService = productService;
        this.productRepository = productRepository;



        storage = StorageOptions.newBuilder().setCredentials(GoogleStorageService.generateCredentials())
                .build()
                .getService();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {

        List<Product> products = productService.getAllProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("by-id/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("quantity") Integer quantity) {

        try {
            Optional<Product> existingProductOptional = productRepository.findById(id);

            if (existingProductOptional.isPresent()) {
                Product existingProduct = existingProductOptional.get();
                existingProduct.setName(name);
                existingProduct.setDescription(description);
                existingProduct.setPrice(price);
                existingProduct.setQuantity(quantity);

                if (!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    BlobId blobId = BlobId.of(bucketName, fileName);
                    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                    storage.create(blobInfo, file.getBytes());
                    String imageUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;
                    existingProduct.setImageUrl(imageUrl);
                }

                productRepository.save(existingProduct);
                return ResponseEntity.ok("Product updated successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update product.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("quantity") Integer quantity) {

        try {
            String fileName = file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, file.getBytes());

            String imageUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImageUrl(imageUrl);
            productRepository.save(product);

            return ResponseEntity.ok("Product with image uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image or save product.");
        }
    }

    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") long productId,
            @RequestParam Integer quantity
    ) {
        productService.reduceQuantity(productId,quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}