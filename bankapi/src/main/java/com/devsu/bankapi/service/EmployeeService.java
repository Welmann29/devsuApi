package com.devsu.bankapi.service;

import com.devsu.bankapi.entities.Employee;
import com.devsu.bankapi.repositories.EmployeeRepository;
import com.devsu.bankapi.utils.dto.response.EmployeeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository
    ){
        this.employeeRepository = employeeRepository;
    }


    public EmployeeResponse createEmployee(){
        Employee employee = new Employee();
        employee.setDocumentType("D");
        employee.setDocument("2070654460117");
        employee.setFirstName("Welmann");
        employee.setFirstSurname("Paniagua");
        employee.setGender("M");
        employee.setDirection("4ta av");
        employee.setPhoneNumber("41335696");
        employee.setCurrentAgency(1);
        employee.setEmployeeStatus(true);
        employeeRepository.save(employee);

        return new EmployeeResponse();

    }

}
