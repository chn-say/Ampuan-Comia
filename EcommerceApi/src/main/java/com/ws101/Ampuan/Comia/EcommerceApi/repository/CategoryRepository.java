package com.ws101.Ampuan.Comia.EcommerceApi.repository;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Pwede mo itong gamitin para maghanap ng category gamit ang pangalan nito
    Category findByName(String name);
}