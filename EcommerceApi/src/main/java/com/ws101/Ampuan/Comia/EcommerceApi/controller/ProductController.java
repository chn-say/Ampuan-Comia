package com.ws101.Ampuan.Comia.EcommerceApi.controller;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Product;
import com.ws101.Ampuan.Comia.EcommerceApi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET /api/v1/products - Returns 200 OK
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // GET /api/v1/products/{id} - Returns 200 OK or 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/v1/products/filter - Returns 200 OK
    @GetMapping("/filter")
    public List<Product> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {

        String category = filterType.equalsIgnoreCase("category") ? filterValue : null;
        Double maxPrice = filterType.equalsIgnoreCase("price") ? Double.parseDouble(filterValue) : null;
        String name = filterType.equalsIgnoreCase("name") ? filterValue : null;

        return productService.filterProducts(category, maxPrice, name);
    }

    // POST /api/v1/products - Returns 201 Created (Tugma sa Task 5.1)
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // PUT /api/v1/products/{id} - Returns 200 OK or 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/v1/products/{id} - Returns 200 OK or 404 Not Found
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partiallyUpdateProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return productService.getProductById(id).map(existingProduct -> {
            if (updates.containsKey("name")) existingProduct.setName((String) updates.get("name"));
            if (updates.containsKey("description")) existingProduct.setDescription((String) updates.get("description"));
            if (updates.containsKey("price")) existingProduct.setPrice(Double.parseDouble(updates.get("price").toString()));
            if (updates.containsKey("category")) existingProduct.setCategory((String) updates.get("category"));
            if (updates.containsKey("stock")) existingProduct.setStock(Integer.parseInt(updates.get("stock").toString()));
            if (updates.containsKey("imageUrl")) existingProduct.setImageUrl((String) updates.get("imageUrl"));

            return ResponseEntity.ok(existingProduct);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/products/{id} - Returns 204 No Content or 404 Not Found
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}