package com.example.productservice.controller;

import com.example.productservice.entity.ImageData;
import com.example.productservice.entity.Product;
import com.example.productservice.model.ImageDataDTO;
import com.example.productservice.model.ProductWithImagesDTO;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<String> createProductWithImage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            Long productId = productService.createProductWithImage(name, description, imageFile);
            return ResponseEntity.ok().body("Product created with ID: " + productId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product");
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProductWithImages(@PathVariable Long productId) {
        productService.deleteProductWithImages(productId);
        return ResponseEntity.ok().body("Product and associated images deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductWithImagesDTO>> getAllProductsWithImages() {
        List<Product> products = productService.getAllProducts();
        List<ProductWithImagesDTO> productsWithImages = products.stream()
                .map(this::mapProductToProductWithImagesDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(productsWithImages);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProductWithImageAndFields(
            @PathVariable Long productId,
            @RequestParam(value = "name", required = false) String newName,
            @RequestParam(value = "description", required = false) String newDescription,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        try {
            Product updatedProduct = productService.updateProductWithImageAndFields(productId, newName, newDescription, imageFile);
            return ResponseEntity.ok().body("Product updated with ID: " + updatedProduct.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product");
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductWithImagesDTO> getProductWithImageById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            ProductWithImagesDTO productWithImages = mapProductToProductWithImagesDTO(product);
            return ResponseEntity.ok().body(productWithImages);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ProductWithImagesDTO mapProductToProductWithImagesDTO(Product product) {
        ProductWithImagesDTO productWithImages = new ProductWithImagesDTO();
        productWithImages.setId(product.getId());
        productWithImages.setName(product.getName());
        productWithImages.setDescription(product.getDescription());

        if (product.getImageData() != null) {
            ImageData imageData = product.getImageData();
            ImageDataDTO imageDataDTO = new ImageDataDTO();
            imageDataDTO.setName(imageData.getName());
            imageDataDTO.setType(imageData.getType());
            productWithImages.setImageData(imageDataDTO);
        }

        return productWithImages;
    }
}
