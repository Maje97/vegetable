package com.project.vegetable.service;

import com.project.vegetable.exception.InsufficientStockException;
import com.project.vegetable.exception.OrderNotFoundException;
import com.project.vegetable.exception.OrderItemNotFoundException;
import com.project.vegetable.model.*;
import com.project.vegetable.repository.OrderRepository;
import com.project.vegetable.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Service
public class OrderService {

    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(ProductService productService,
                        CustomerService customerService,
                        OrderRepository orderRepository) {
        this.productService = productService;
        this.customerService = customerService;
        this.orderRepository = orderRepository;
    }

    public void validateOrderItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }

        for (OrderItem item : items) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be more than zero.");
            }

            Long productId = item.getProduct() != null ? item.getProduct().getId() : null;
            if (productId == null) {
                throw new IllegalArgumentException("Product ID is missing in order item.");
            }

            Product product = productService.getProductById(productId);
            item.setProduct(product);

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new InsufficientStockException(
                        product.getId(),
                        item.getQuantity(),
                        product.getStockQuantity());
            }
        }
    }

    public BigDecimal calculateOrderTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Order createOrder(Long customerId, List<OrderItem> items) {
        Customer customer = customerService.getCustomerById(customerId);
        validateOrderItems(items);
        BigDecimal total = calculateOrderTotal(items);

        Order order = new Order();
        order.setCustomer(customer);
        order.setTotalAmount(total);
        order.setOrderDate(LocalDateTime.now());

        order = orderRepository.save(order);

        for (OrderItem item : items) {
            Product product = item.getProduct(); // make sure this is already fetched and not null
            if (product == null || product.getId() == null) {
                throw new IllegalArgumentException("Product must be set and persisted before creating order item.");
            }

            item.setOrder(order);
            item.setId(new OrderItemId(order.getId(), product.getId()));
        }

        order.setOrderItems(items);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
