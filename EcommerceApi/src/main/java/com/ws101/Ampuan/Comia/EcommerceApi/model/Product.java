package com.ws101.Ampuan.Comia.EcommerceApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Ito ang magic ng Lombok: Gagawa na ito ng Getters, Setters, toString, atbp.
@NoArgsConstructor // Gagawa ng empty constructor
@AllArgsConstructor // Gagawa ng constructor na may lahat ng fields
public class Product {

    private Long id;              // Unique identifier (ID)
    private String name;          // Product name
    private String description;   // Description
    private Double price;         // Price
    private String category;      // Category
    private Integer stockQuantity; // Stock quantity
    private String imageUrl;      // Image URL (optional)

}