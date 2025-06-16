package com.project.vegetable.service;

import com.project.vegetable.exception.CustomerNotFoundException;
import com.project.vegetable.exception.ProductNotFoundException;
import com.project.vegetable.model.Customer;
import com.project.vegetable.model.Order;
import com.project.vegetable.model.Product;
import com.project.vegetable.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        existingCustomer.setEmail(updatedCustomer.getEmail());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public List<Order> getAllOrdersForCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return customer.getOrders();
    }

    public Map<String, String> getCustomerContactInfo(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        Map<String, String> info = new HashMap<>();
        info.put("address", customer.getAddress());
        info.put("email", customer.getEmail());
        return info;
    }
}
