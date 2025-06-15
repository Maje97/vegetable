package com.project.vegetable.repository;

import com.project.vegetable.model.Order;
import com.project.vegetable.model.OrderItem;
import com.project.vegetable.model.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalDouble;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrder(Order order);
    Optional<OrderItem> findById(OrderItemId id);

    void deleteById(OrderItemId id);
}
