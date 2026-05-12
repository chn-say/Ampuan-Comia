package com.ws101.Ampuan.Comia.EcommerceApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/products") // Ang URL ay magiging localhost:8080/api/products
public class ProductController {

    @Autowired
    private com.ws101.Ampuan.Comia.EcommerceApi.service.ProductService productService;

    @GetMapping
    public List<com.ws101.iyonglastname.ecommerceapi.model.Product> getProducts() {
        return productService.getAllProducts();
    }
}