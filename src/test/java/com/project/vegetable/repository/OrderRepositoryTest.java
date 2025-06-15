package com.project.vegetable.repository;

import com.project.vegetable.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void testSaveAndFindOrder() {
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findByName("Potato")
                .orElseThrow(() -> new RuntimeException("Product not found"));

        LocalDateTime orderDate = LocalDateTime.now();

        Order order = new Order();
        order.setTotalAmount(20.00);
        order.setCustomer(customer);
        order.setOrderDate(orderDate);
        orderRepository.saveAndFlush(order);

        OrderItemId itemId = new OrderItemId(order.getId(), product.getId());

        OrderItem orderItem = new OrderItem();
        orderItem.setId(itemId);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(20);
        orderItem.setPriceAtPurchase(1.00F);
        orderItemRepository.save(orderItem);

        order.addOrderItem(orderItem);

        orderRepository.saveAndFlush(order);

        Optional<Order> found = orderRepository.findById(order.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTotalAmount()).isEqualTo(20.00);
        assertThat(found.get().getOrderDate()).isEqualTo(orderDate);
    }

    @Test
    public void testCascadeDeleteOrder() {
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

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(20);
        orderItem.setPriceAtPurchase(1.00F);
        orderItem.setId(new OrderItemId());

        order.addOrderItem(orderItem);

        orderRepository.saveAndFlush(order);

        orderRepository.deleteById(order.getId());
        orderRepository.flush();
        assertThat(orderRepository.findById(order.getId())).isNotPresent();
        assertThat(customerRepository.findById(customer.getId())).isPresent();
        assertThat(orderItemRepository.findById(orderItem.getId())).isNotPresent();
    }

    @Test
    public void testOrphanRemoval() {
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findByName("Potato")
                .orElseThrow(() -> new RuntimeException("Product not found"));

        LocalDateTime orderDate = LocalDateTime.now();

        Order order = new Order();
        order.setTotalAmount(20.00);
        order.setCustomer(customer);
        order.setOrderDate(orderDate);
        orderRepository.saveAndFlush(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(20);
        orderItem.setPriceAtPurchase(1.00F);
        orderItem.setId(new OrderItemId());

        order.addOrderItem(orderItem);
        orderRepository.saveAndFlush(order);

        order.removeOrderItem(orderItem);
        orderRepository.saveAndFlush(order);

        assertThat(orderRepository.findById(order.getId())).isPresent();
        assertThat(customerRepository.findById(customer.getId())).isPresent();
        assertThat(orderItemRepository.findById(orderItem.getId())).isNotPresent();
    }
}
