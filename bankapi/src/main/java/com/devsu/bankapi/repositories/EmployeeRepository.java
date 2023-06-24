package com.devsu.bankapi.repositories;

import com.devsu.bankapi.entities.Employee;
import com.devsu.bankapi.entities.keys.PersonKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends
        JpaRepository<Employee, PersonKey>,
        JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeCode(Integer employeeCode);

    Boolean existsByEmployeeCode(Integer employeeCode);

}
