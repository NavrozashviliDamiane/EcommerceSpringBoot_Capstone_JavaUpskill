package com.example.productservice.service;

import com.example.productservice.entity.Product;

import java.util.List;

public interface ProductSearchAndSortService {
    List<Product> searchByName(String keyword);

    List<Product> searchByDescription(String keyword);

    List<Product> searchByPriceRange(Double minPrice, Double maxPrice);

    List<Product> filterByPriceLessThanOrEqual(Double price);

    List<Product> filterByPriceGreaterThanOrEqual(Double price);

    List<Product> findByNameContaining(String partialName);

    List<Product> findByDescriptionContaining(String partialDescription);

    List<Product> findProductsFuzzy(String fuzzyName);
}
