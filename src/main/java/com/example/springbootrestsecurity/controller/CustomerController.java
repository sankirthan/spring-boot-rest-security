package com.example.springbootrestsecurity.controller;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.springbootrestsecurity.jpa.entity.Customer;
import com.example.springbootrestsecurity.jpa.repository.CustomerRepository;

@Api
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;
	
	@GetMapping
	public Page<Customer> getAllCustomer(
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
		
		if(page != null & size != null) {
			if(page < 0 || size < 1) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
						"page must be greater than equl to 0, and size must be greater than 0");
			}
			else {
				return customerRepository.findAll(PageRequest.of(page, size, Sort.by("lastName")));
			}
		}
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page and size must be provided");
	}
	
	
	 @GetMapping("{id}")
	 public Customer getById(@PathVariable("id") Long id) {
	    return customerRepository.findById(id)
	    		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
	    													String.format("Customer with id %s not found", id)));
	 }
	 
	 @PostMapping
	 public Customer createCustomer(@RequestBody Customer customer) {
		 return customerRepository.save(customer);
	 }
	 
	 @PutMapping("{id}")
	 public Customer updateCustomerById(@PathVariable("id") Long id, @RequestBody Customer customer) { 
		 return customerRepository.findById(id)
				 	.map(existingCustomer -> {
				 		existingCustomer.setFirstName(customer.getFirstName());
				 		existingCustomer.setLastName(customer.getLastName());
				 		return customerRepository.save(existingCustomer);
				 	})
				 	.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
							String.format("Customer with id %s not found", id)));
	 }
	 
	 @DeleteMapping("{id}")
	 public void deleteEmployee(@PathVariable("id") Long id) {
		 customerRepository.deleteById(id);
	 }
}
