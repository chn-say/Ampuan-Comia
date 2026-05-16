package com.ws101.Ampuan.Comia.EcommerceApi.repository;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Custom Finder Method gamit ang Method Naming base sa lab task instruction
    List<Product> findByCategoryName(String name);

    List<Product> findByPriceLessThanEqual(Double price);

    List<Product> findByNameContainingIgnoreCase(String name);

    // 2. Custom Filter gamit ang @Query at JPQL para sa Price Range
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> findProductsByPriceRange(@Param("min") Double min, @Param("max") Double max);
}