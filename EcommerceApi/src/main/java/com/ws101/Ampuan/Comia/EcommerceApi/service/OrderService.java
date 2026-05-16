package com.ws101.Ampuan.Comia.EcommerceApi.service;

import com.ws101.Ampuan.Comia.EcommerceApi.model.Order;
import com.ws101.Ampuan.Comia.EcommerceApi.model.OrderItem;
import com.ws101.Ampuan.Comia.EcommerceApi.model.Product;
import com.ws101.Ampuan.Comia.EcommerceApi.repository.OrderRepository;
import com.ws101.Ampuan.Comia.EcommerceApi.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(Order order) {
        double totalAmount = 0;

        for (OrderItem item : order.getOrderItems()) {
            // Gumamit ng EntityNotFoundException para gumana ang 404 global handler
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + item.getProduct().getId()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // Bawasan ang stock sa database
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            // Set ang details para sa order item row
            item.setProduct(product);
            item.setPrice(product.getPrice() * item.getQuantity());
            item.setOrder(order);

            totalAmount += item.getPrice();
        }

        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }
}