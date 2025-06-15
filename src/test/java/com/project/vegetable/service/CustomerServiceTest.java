package com.project.vegetable.service;

import com.project.vegetable.exception.CustomerNotFoundException;
import com.project.vegetable.model.Customer;
import com.project.vegetable.model.Order;
import com.project.vegetable.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setName("Alice");

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer saved = customerService.createCustomer(customer);

        assertEquals("Alice", saved.getName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testGetCustomerById_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Bob");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertEquals("Bob", result.getName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(99L));
    }

    @Test
    void testGetAllOrdersForCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        List<Order> orders = List.of(new Order(), new Order());
        customer.setOrders(orders);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        List<Order> result = customerService.getAllOrdersForCustomer(1L);

        assertEquals(2, result.size());
    }

    @Test
    void testGetCustomerContactInfo() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setAddress("123 Main St");
        customer.setEmail("test@example.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Map<String, String> info = customerService.getCustomerContactInfo(1L);

        assertEquals("123 Main St", info.get("address"));
        assertEquals("test@example.com", info.get("email"));
    }
}
