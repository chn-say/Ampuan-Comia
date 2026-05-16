package com.ws101.Ampuan.Comia.EcommerceApi.controller;

import jakarta.validation.Valid;
import com.ws101.Ampuan.Comia.EcommerceApi.model.Product;
import com.ws101.Ampuan.Comia.EcommerceApi.model.Category;
import com.ws101.Ampuan.Comia.EcommerceApi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class for managing Product API endpoints.
 * Handles HTTP requests for CRUD operations on products.
 * @author Ampuan & Comia
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Retrieves the list of all products from the system.
     * @return A list of all Product objects.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Retrieves a specific product by its unique ID.
     * @param id The ID of the product to retrieve.
     * @return A ResponseEntity containing the Product if found, or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Filters products based on category, price, or name.
     * @param filterType The type of filter to apply (category, price, or name).
     * @param filterValue The value used for filtering.
     * @return A list of products matching the filter criteria.
     */
    @GetMapping("/filter")
    public List<Product> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {

        String category = filterType.equalsIgnoreCase("category") ? filterValue : null;
        Double maxPrice = filterType.equalsIgnoreCase("price") ? Double.parseDouble(filterValue) : null;
        String name = filterType.equalsIgnoreCase("name") ? filterValue : null;

        return productService.filterProducts(category, maxPrice, name);
    }

    /**
     * Task 3 Custom JPQL Endpoint: Retrieves products within a specified price range.
     * @param min The minimum price boundary.
     * @param max The maximum price boundary.
     * @return A list of products matching the criteria.
     */
    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return productService.getProductsByPriceRange(min, max);
    }

    /**
     * Creates and saves a new product in the system.
     * @param product The product details to be created (validated).
     * @return The created Product object with 201 Created status.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product created = productService.createProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Updates all information of an existing product.
     * @param id The ID of the product to update.
     * @param updatedProduct The new data for the product.
     * @return The updated Product object, or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates specific fields of an existing product (Partial Update).
     * @param id The ID of the product to update.
     * @param updates A map containing the fields and values to update.
     * @return The updated Product object.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partiallyUpdateProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return productService.getProductById(id).map(existingProduct -> {
            if (updates.containsKey("name")) existingProduct.setName((String) updates.get("name"));
            if (updates.containsKey("description")) existingProduct.setDescription((String) updates.get("description"));
            if (updates.containsKey("price")) existingProduct.setPrice(Double.parseDouble(updates.get("price").toString()));
            if (updates.containsKey("stock")) existingProduct.setStock(Integer.parseInt(updates.get("stock").toString()));
            if (updates.containsKey("imageUrl")) existingProduct.setImageUrl((String) updates.get("imageUrl"));

            if (updates.containsKey("category")) {
                Category newCategory = new Category();
                newCategory.setName((String) updates.get("category"));
                existingProduct.setCategory(newCategory);
            }

            Product updatedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(updatedProduct);
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a product from the system by its ID.
     * @param id The ID of the product to delete.
     * @return 204 No Content if successful, or 404 if ID is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}