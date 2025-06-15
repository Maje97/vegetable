package com.project.vegetable.repository;

import com.project.vegetable.model.Customer;
import com.project.vegetable.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName("Johan Johansson");
        customer.setAddress("Test 987, 65432 Test");
        customer.setEmail("johan@test.com");
        return customerRepository.saveAndFlush(customer);
    }

    @Test
    public void testSaveAndFindCustomer() {
        Customer customer = createCustomer();

        Optional<Customer> found = customerRepository.findById(customer.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Johan Johansson");
        assertThat(found.get().getAddress()).isEqualTo("Test 987, 65432 Test");
        assertThat(found.get().getEmail()).isEqualTo("johan@test.com");
    }

    @Test
    public void testCascadeDeleteCustomer() {
        Customer customer = createCustomer();

        LocalDateTime orderDate = LocalDateTime.now();

        Order order = new Order();
        order.setTotalAmount(20.00);
        order.setCustomer(customer);
        order.setOrderDate(orderDate);
        orderRepository.save(order);

        customer.addOrder(order);
        customerRepository.saveAndFlush(customer);

        customerRepository.deleteById(customer.getId());
        customerRepository.flush();
        assertThat(customerRepository.findById(customer.getId())).isNotPresent();
        assertThat(orderRepository.findById(order.getId())).isNotPresent();
    }

    @Test
    public void testOrphanRemoval() {
        Customer customer = createCustomer();

        LocalDateTime orderDate = LocalDateTime.now();

        Order order = new Order();
        order.setTotalAmount(20.00);
        order.setCustomer(customer);
        order.setOrderDate(orderDate);
        orderRepository.save(order);

        customer.addOrder(order);
        customerRepository.saveAndFlush(customer);

        customer.removeOrder(order);
        customerRepository.saveAndFlush(customer);

        assertThat(customerRepository.findById(customer.getId())).isPresent();
        assertThat(orderRepository.findById(order.getId())).isNotPresent();
    }
}
