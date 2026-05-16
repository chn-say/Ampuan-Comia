package com.ws101.Ampuan.Comia.EcommerceApi.repository;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}