package com.devsu.bankapi.controller;

import com.devsu.bankapi.utils.dto.response.EmployeeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.bankapi.service.EmployeeService;

@Slf4j
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(
            EmployeeService employeeService
    ) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> getTest(){
        return ResponseEntity.ok(
                employeeService.createEmployee()
        );
    }

}
