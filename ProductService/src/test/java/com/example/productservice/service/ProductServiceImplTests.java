package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceImplTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService = new ProductServiceImpl(productRepository);

    @Test
    public void testGetAllProducts() {

        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Product 1", "Description 1", "image-url-1", 10.0, 100));
        productList.add(new Product(2L, "Product 2", "Description 2", "image-url-2", 15.0, 150));


        when(productRepository.findAll()).thenReturn(productList);


        List<Product> products = productService.getAllProducts();


        assertEquals(2, products.size());

    }


}
