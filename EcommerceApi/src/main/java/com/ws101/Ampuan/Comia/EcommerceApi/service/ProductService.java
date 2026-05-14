package com.ws101.Ampuan.Comia.EcommerceApi.service;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        // Initialize 10 sample products - Tugma sa Product model constructor
        products.add(new Product(1L, "shoes", "comfortable to wear", 450.0, "shoes", 10, "url-to-image"));
        products.add(new Product(2L, "sandals", "aesthetic for women", 300.0, "shoes", 50, "url-to-image"));
        products.add(new Product(3L, "t-shirt", "100% pure cotton", 250.0, "clothing", 100, "url-to-image"));
        products.add(new Product(4L, "hoodie", "thick and warm jacket", 550.0, "clothing", 40, "url-to-image"));
        products.add(new Product(5L, "socks", "soft cotton socks", 50.0, "shoes", 200, "url-to-image"));
        products.add(new Product(6L, "cap", "adjustable baseball cap", 150.0, "accessories", 30, "url-to-image"));
        products.add(new Product(7L, "backpack", "waterproof travel bag", 850.0, "accessories", 15, "url-to-image"));
        products.add(new Product(8L, "sunglasses", "uv400 protection", 120.0, "accessories", 60, "url-to-image"));
        products.add(new Product(9L, "watch", "water-resistant analog watch", 1200.0, "accessories", 12, "url-to-image"));
        products.add(new Product(10L, "shorts", "casual summer wear shorts", 180.0, "clothing", 80, "url-to-image"));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Product createProduct(Product product) {
        Long nextId = products.stream()
                .mapToLong(Product::getId)
                .max()
                .orElse(0L) + 1;
        product.setId(nextId);
        products.add(product);
        return product;
    }

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return getProductById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setStock(updatedProduct.getStock());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());
            return existingProduct;
        });
    }

    public boolean deleteProduct(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    public List<Product> filterProducts(String category, Double maxPrice, String name) {
        return products.stream()
                .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .filter(p -> name == null || p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}