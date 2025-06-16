package com.project.vegetable.controller;

import com.project.vegetable.model.Customer;
import com.project.vegetable.model.Order;
import com.project.vegetable.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //GET
    @GetMapping
    @Operation(summary = "Get all customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by id")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Get a customers orders by customer id")
    public List<Order> getCustomersOrders(@PathVariable Long id) {
        return customerService.getAllOrdersForCustomer(id);
    }

    @GetMapping("/contacts/{id}")
    @Operation(summary = "Get a customers orders by customer id")
    public Map<String, String> getCustomersContactInfo(@PathVariable Long id) {
        return customerService.getCustomerContactInfo(id);
    }

    //POST
    @PostMapping
    @Operation(summary = "Create new customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    //PUT
    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer) {
        Customer updated = customerService.updateCustomer(id, customer);
        return ResponseEntity.ok(updated);
    }

    //DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
