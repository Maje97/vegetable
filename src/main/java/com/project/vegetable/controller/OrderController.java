package com.project.vegetable.controller;

import com.project.vegetable.model.Order;
import com.project.vegetable.model.OrderItem;
import com.project.vegetable.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //GET
    @GetMapping
    @Operation(summary = "Get all orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    //POST
    @PostMapping("/{id}")
    @Operation(summary = "Create order")
    public ResponseEntity<Order> createOrder(
            @PathVariable Long id,
            @RequestBody List<OrderItem> items) {
        Order savedOrder = orderService.createOrder(id, items);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    //DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order by id")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
