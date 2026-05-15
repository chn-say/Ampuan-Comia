package com.ws101.Ampuan.Comia.EcommerceApi.repository;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryContainingIgnoreCase(String category);
    List<Product> findByPriceLessThanEqual(Double price);
    List<Product> findByNameContainingIgnoreCase(String name);
}