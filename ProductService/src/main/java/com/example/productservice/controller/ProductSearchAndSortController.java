package com.example.productservice.controller;


import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductSearchAndSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product/search-sort")
public class ProductSearchAndSortController {

    @Autowired
    private ProductSearchAndSortService productSearchAndSortService;

    @GetMapping("/searchByName")
    public List<Product> searchByName(@RequestParam String keyword) {
        return productSearchAndSortService.searchByName(keyword);
    }

    @GetMapping("/searchByDescription")
    public List<Product> searchByDescription(@RequestParam String keyword) {
        return productSearchAndSortService.searchByDescription(keyword);
    }

    @GetMapping("/searchByPriceRange")
    public List<Product> searchByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return productSearchAndSortService.searchByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("/filterByPriceLessThanOrEqual")
    public List<Product> filterByPriceLessThanOrEqual(@RequestParam Double price) {
        return productSearchAndSortService.filterByPriceLessThanOrEqual(price);
    }

    @GetMapping("/filterByPriceGreaterThanOrEqual")
    public List<Product> filterByPriceGreaterThanOrEqual(@RequestParam Double price) {
        return productSearchAndSortService.filterByPriceGreaterThanOrEqual(price);
    }

    // Partial Matches
    @GetMapping("/findByNameContaining")
    public List<Product> findByNameContaining(@RequestParam String partialName) {
        return productSearchAndSortService.findByNameContaining(partialName);
    }

    @GetMapping("/findByDescriptionContaining")
    public List<Product> findByDescriptionContaining(@RequestParam String partialDescription) {
        return productSearchAndSortService.findByDescriptionContaining(partialDescription);
    }

    @GetMapping("/searchFuzzy")
    public List<Product> searchProductsFuzzy(@RequestParam String fuzzyName) {
        return productSearchAndSortService.findProductsFuzzy(fuzzyName);
    }
}
