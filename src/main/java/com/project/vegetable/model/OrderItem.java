package com.project.vegetable.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;

    private Integer quantity;

    @Column(name = "price_at_purchase")
    private BigDecimal priceAtPurchase;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    // Constructors
    public OrderItem() {}

    // Getters and Setters
    public OrderItemId getId() {
        return id;
    }

    public void setId(OrderItemId id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getProductId() {
        return product.getId();
    }
}
