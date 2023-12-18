package com.example.productservice.product;

import com.example.productservice.ProductServiceApplication;
import com.example.productservice.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Product.class)
@ExtendWith(MockitoExtension.class)
public class ProductTest {

    @Test
    public void testConstructor() {
        Product product = new Product("Test Product", "This is a test product", "test-image.jpg", 9.99, 10);
        assertNull(product.getId()); // Since ID is auto-generated by JPA, it should be null
        assertEquals("Test Product", product.getName());
        assertEquals("This is a test product", product.getDescription());
        assertEquals("test-image.jpg", product.getImageUrl());
        assertEquals(9.99, product.getPrice());
        assertEquals(10, product.getQuantity());
    }

    @Test
    public void testGettersAndSetters() {
        Product product = new Product();
        product.setId(1L);
        assertEquals(1L, product.getId());
        product.setName("New Name");
        assertEquals("New Name", product.getName());
        product.setDescription("New Description");
        assertEquals("New Description", product.getDescription());
        product.setImageUrl("new-image.jpg");
        assertEquals("new-image.jpg", product.getImageUrl());
        product.setPrice(19.99);
        assertEquals(19.99, product.getPrice());
        product.setQuantity(20);
        assertEquals(20, product.getQuantity());
    }

    @Test
    public void testSetNameToNull() {
        assertThrows(NullPointerException.class, () -> new Product().setName(null));
    }



    @Test
    public void testSetPriceToNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Product().setPrice(-1.0));
    }





}