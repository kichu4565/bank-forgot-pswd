package com.bms.repository;

import com.bms.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByIdNumber(String idNumber);
    boolean existsByIdNumber(String idNumber);
} 