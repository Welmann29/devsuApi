package com.devsu.bankapi.repositories;

import com.devsu.bankapi.entities.Customer;
import com.devsu.bankapi.entities.keys.PersonKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends
        JpaRepository<Customer, PersonKey>,
        JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByCustomerCode(Long customerCode);

    Boolean existsByCustomerCodeAndCustomerStatus(Long customerCode, String customerStatus);

}
