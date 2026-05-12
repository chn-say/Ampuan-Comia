package com.ws101.Ampuan.Comia.EcommerceApi.service;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    // Temporary list (mock database)
    private List<Product> products = new ArrayList<>();

    public ProductService() {
        // sample data
        products.add(new Product(1L, "Laptop", "High-end gaming laptop", 75000.0, "Electronics", 10, "url-to-image"));
        products.add(new Product(2L, "Mouse", "Wireless optical mouse", 1500.0, "Electronics", 50, "url-to-image"));
    }

    public List<Product> getAllProducts() {
        return products;
    }
}