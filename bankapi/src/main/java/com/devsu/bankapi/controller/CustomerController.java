package com.devsu.bankapi.controller;

import com.devsu.bankapi.repositories.CustomerRepository;
import com.devsu.bankapi.service.CustomerService;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.dto.general.PageableResponse;
import com.devsu.bankapi.utils.dto.request.CreateCustomerRequest;
import com.devsu.bankapi.utils.dto.response.CustomerResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static com.devsu.bankapi.utils.functions.CommonErrors.validationException;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService
    ){
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody @Valid CreateCustomerRequest request
    ){
        CustomerResponse res = customerService.createCustomer(request);

        if (res.getSuccess()){
            return ResponseEntity.status(201).body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @GetMapping
    public  ResponseEntity<CustomerResponse> getCustomerById(
            @RequestParam String type,
            @RequestParam String code
    ){
        CustomerResponse res = customerService.getByCode(type, code);
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> updateCustomer(
            @RequestBody @Valid CreateCustomerRequest request
    ){
        CustomerResponse res = customerService.updateCustomer(request);
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @DeleteMapping
    public ResponseEntity<AbstractResponse> deleteCustomer(
            @RequestParam String type,
            @RequestParam String code
    ){
        AbstractResponse res = customerService.deleteCustomer(
                type, code
        );
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PatchMapping("/reactivateCustomer")
    public  ResponseEntity<CustomerResponse> reactivate(
            @RequestParam String type,
            @RequestParam String code
    ){
        CustomerResponse res = customerService.reactivateCustomer(type, code);
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @GetMapping("/findAllPageable")
    public  ResponseEntity<PageableResponse> getAllCustomers(
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        PageableResponse res = customerService.getAllPageable(page, size);
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AbstractResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        return validationException(ex);
    }

}
