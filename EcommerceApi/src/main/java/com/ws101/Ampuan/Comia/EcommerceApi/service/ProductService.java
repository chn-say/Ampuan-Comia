package com.ws101.Ampuan.Comia.EcommerceApi.service;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private List<Product> products = new ArrayList<>();

    public ProductService() {
        products.add(new Product(1L, "shoes", "comfortable to wear", 450.0, "shoes", 10, "url-to-image"));
        products.add(new Product(2L, "sandals", "aesthetic for women", 300.0, "shoes", 50, "url-to-image"));
    }

    public List<Product> getAllProducts() {
        return products;
    }
}