package com.example.springbootrestsecurity.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springbootrestsecurity.jpa.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
}
