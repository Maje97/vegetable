package com.project.vegetable.repository;

import com.project.vegetable.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
