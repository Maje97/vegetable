package com.project.vegetable.repository;

import com.project.vegetable.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderItemRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void testSaveAndFindOrderItem() {
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findByName("Potato")
                .orElseThrow(() -> new RuntimeException("Product not found"));

        LocalDateTime orderDate = LocalDateTime.now();

        Order order = new Order();
        order.setTotalAmount(20.00);
        order.setCustomer(customer);
        order.setOrderDate(orderDate);
        orderRepository.save(order);

        OrderItemId itemId = new OrderItemId(order.getId(), product.getId());
        OrderItem orderItem = new OrderItem();
        orderItem.setId(itemId);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(20);
        orderItem.setPriceAtPurchase(1.00F);

        orderItemRepository.saveAndFlush(orderItem);

        Optional<OrderItem> found = orderItemRepository.findById(itemId);
        assertThat(found).isPresent();
        assertThat(found.get().getQuantity()).isEqualTo(20);
        assertThat(found.get().getOrder()).isEqualTo(order);
        assertThat(found.get().getProduct()).isEqualTo(product);
    }

    @Test
    public void testDeleteOrderItem() {
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findByName("Potato")
                .orElseThrow(() -> new RuntimeException("Product not found"));

        LocalDateTime orderDate = LocalDateTime.now();

        Order order = new Order();
        order.setTotalAmount(20.00);
        order.setCustomer(customer);
        order.setOrderDate(orderDate);
        orderRepository.save(order);

        OrderItemId itemId = new OrderItemId(order.getId(), product.getId());
        OrderItem orderItem = new OrderItem();
        orderItem.setId(itemId);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(20);
        orderItem.setPriceAtPurchase(1.00F);

        orderItemRepository.saveAndFlush(orderItem);

        orderItemRepository.deleteById(itemId);
        orderItemRepository.flush();

        assertThat(orderItemRepository.findById(itemId)).isNotPresent();
        assertThat(customerRepository.findById(customer.getId())).isPresent();
        assertThat(productRepository.findById(product.getId())).isPresent();
    }
}
