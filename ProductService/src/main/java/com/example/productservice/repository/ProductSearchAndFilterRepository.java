package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSearchAndFilterRepository  extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByDescriptionContainingIgnoreCase(String keyword);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByPriceLessThanEqual(Double price);

    List<Product> findByPriceGreaterThanEqual(Double price);

    // Partial Matches
    List<Product> findByNameContaining(String partialName);

    List<Product> findByDescriptionContaining(String partialDescription);

    @Query(value = "SELECT * FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :fuzzyName, '%'))", nativeQuery = true)
    List<Product> findByNameFuzzy(String fuzzyName);

}
