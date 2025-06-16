package com.project.vegetable.service;

import com.project.vegetable.exception.InsufficientStockException;
import com.project.vegetable.exception.OrderNotFoundException;
import com.project.vegetable.exception.ProductNotFoundException;
import com.project.vegetable.model.Customer;
import com.project.vegetable.model.Order;
import com.project.vegetable.model.OrderItem;
import com.project.vegetable.model.Product;
import com.project.vegetable.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private ProductService productService;
    private CustomerService customerService;
    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        customerService = mock(CustomerService.class);
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(productService, customerService, orderRepository);
    }

    @Test
    void validateOrderItems_validItems_doesNotThrow() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setPriceAtPurchase(BigDecimal.valueOf(2.00));

        when(productService.getProductById(1L)).thenReturn(product);

        assertDoesNotThrow(() -> orderService.validateOrderItems(List.of(item)));
    }

    @Test
    void validateOrderItems_emptyList_throwsIllegalArgumentException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            orderService.validateOrderItems(List.of());
        });
        assertEquals("Order must contain at least one item.", ex.getMessage());
    }

    @Test
    void validateOrderItems_invalidQuantity_throwsIllegalArgumentException() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(0); // <- Not OK!
        item.setPriceAtPurchase(BigDecimal.valueOf(2.00));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            orderService.validateOrderItems(List.of(item));
        });
        assertEquals("Quantity must be more than zero.", ex.getMessage());
    }

    @Test
    void validateOrderItems_insufficientStock_throwsInsufficientStockException() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(20); // <- More than stock quantity!
        item.setPriceAtPurchase(BigDecimal.valueOf(2.00));

        when(productService.getProductById(1L)).thenReturn(product);

        InsufficientStockException ex = assertThrows(InsufficientStockException.class, () -> {
            orderService.validateOrderItems(List.of(item));
        });

        assertTrue(ex.getMessage().contains("insufficient stock"));
    }

    @Test
    void testCreateOrder() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setPriceAtPurchase(BigDecimal.valueOf(2.00));

        Order order = new Order();
        order.setOrderItems(List.of(item));
        order.setCustomer(customer);
        order.setTotalAmount(BigDecimal.valueOf(4.00));

        when(productService.getProductById(1L)).thenReturn(product);
        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order saved = orderService.createOrder(1L, List.of(item));

        assertEquals(customer, saved.getCustomer());
        assertEquals(List.of(item), saved.getOrderItems());
    }

    @Test
    void testGetOrder_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setPriceAtPurchase(BigDecimal.valueOf(2.00));

        Order order = new Order();
        order.setId(1L);
        order.setOrderItems(List.of(item));
        order.setCustomer(customer);
        order.setTotalAmount(BigDecimal.valueOf(4.00));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertEquals(customer, result.getCustomer());
    }

    @Test
    void testGetOrder_NotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(99L));
    }

    @Test
    void testDeleteProduct() {
        Long orderId = 1L;
        orderService.deleteOrder(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
