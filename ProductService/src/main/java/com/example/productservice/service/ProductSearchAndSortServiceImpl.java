package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductSearchAndFilterRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductSearchAndSortServiceImpl implements ProductSearchAndSortService{

    @Autowired
    private ProductSearchAndFilterRepository productSearchAndFilterRepository;

    @Override
    public List<Product> searchByName(String keyword) {
        return productSearchAndFilterRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> searchByDescription(String keyword) {
        return productSearchAndFilterRepository.findByDescriptionContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> searchByPriceRange(Double minPrice, Double maxPrice) {
        return productSearchAndFilterRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Product> filterByPriceLessThanOrEqual(Double price) {
        return productSearchAndFilterRepository.findByPriceLessThanEqual(price);
    }

    @Override
    public List<Product> filterByPriceGreaterThanOrEqual(Double price) {
        return productSearchAndFilterRepository.findByPriceGreaterThanEqual(price);
    }

    @Override
    public List<Product> findByNameContaining(String partialName) {
        return productSearchAndFilterRepository.findByNameContaining(partialName);
    }

    @Override
    public List<Product> findByDescriptionContaining(String partialDescription) {
        return productSearchAndFilterRepository.findByDescriptionContaining(partialDescription);
    }

    @Override
    public List<Product> findProductsFuzzy(String fuzzyName) {
        return productSearchAndFilterRepository.findByNameFuzzy(fuzzyName);
    }
}
