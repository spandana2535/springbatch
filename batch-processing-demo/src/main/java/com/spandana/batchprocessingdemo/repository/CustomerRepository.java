package com.spandana.batchprocessingdemo.repository;
import com.spandana.batchprocessingdemo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> { }