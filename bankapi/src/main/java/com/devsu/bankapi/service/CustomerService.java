package com.devsu.bankapi.service;

import com.devsu.bankapi.entities.Customer;
import com.devsu.bankapi.entities.keys.PersonKey;
import com.devsu.bankapi.repositories.AccountRepository;
import com.devsu.bankapi.repositories.CustomerRepository;
import com.devsu.bankapi.repositories.EmployeeRepository;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.dto.general.PageableResponse;
import com.devsu.bankapi.utils.dto.request.CreateCustomerRequest;
import com.devsu.bankapi.utils.dto.response.CustomerResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.devsu.bankapi.utils.functions.CommonErrors.*;
import static com.devsu.bankapi.utils.functions.CommonFunctions.fillNonEditableFields;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final AccountRepository accountRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            ModelMapper modelMapper,
            AccountRepository accountRepository
    ){
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request){
        ErrorList errors = new ErrorList();
        PersonKey key = new PersonKey(request.getDocumentType(), request.getDocument());
        if (employeeRepository.existsByEmployeeCode(request.getFunctionary())){
            Optional<Customer> optionalCustomer = customerRepository.findById(key);
            if (optionalCustomer.isEmpty()) {
                Customer customer = this.modelMapper.map(request, Customer.class);
                try {
                    customer = customerRepository.save(customer);
                    return this.modelMapper.map(customer, CustomerResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    errors.add(databaseError("crear cliente", e));
                }
            }else {
                if (optionalCustomer.get().getCustomerStatus().equalsIgnoreCase("A")) {
                    errors.add(duplicateEntityByKey("cliente"));
                }else{
                    errors.add(inactiveEntity("cliente"));
                }
            }
        }else{
            errors.add(functionaryNonexistent("creación de cliente"));
        }
        return new CustomerResponse(errors);
    }

    public CustomerResponse getByCode(String type, String code){
        ErrorList errors = new ErrorList();
        Optional<Customer> optionalCustomer;
        if (type.equalsIgnoreCase("Z")){
            optionalCustomer = customerRepository.findByCustomerCode((long) Integer.parseInt(code));
        }else{
            optionalCustomer = customerRepository.findById(new PersonKey(type, code));
        }
        if (optionalCustomer.isPresent()){
            return modelMapper.map(optionalCustomer.get(), CustomerResponse.class);
        }else {
            errors.add(entityNotFound("cliente"));
        }
        return new CustomerResponse(errors);
    }

    public PageableResponse getAllPageable(Integer page, Integer size){
        if (page == 0 && size == 0){
            List<Customer> customers = customerRepository.findAll();
            return new PageableResponse(
                    customers
                            .stream()
                            .map((entity) -> modelMapper.map(entity, CustomerResponse.class))
                            .collect(Collectors.toList()),
                    0,
                    (long) customers.size(),
                    customers.size()
            );
        }else{
            Pageable paging = PageRequest.of(page, size);
            Page<Customer> customersPaging = customerRepository.findAll(paging);
            return new PageableResponse(
                    customersPaging.getContent()
                            .stream()
                            .map((entity) -> modelMapper.map(entity, CustomerResponse.class))
                            .collect(Collectors.toList()),
                    customersPaging.getNumber(),
                    customersPaging.getTotalElements(),
                    customersPaging.getTotalPages()
            );
        }
    }

    public CustomerResponse updateCustomer(CreateCustomerRequest request){
        ErrorList errors = new ErrorList();
        PersonKey key = new PersonKey(request.getDocumentType(), request.getDocument());
        Optional<Customer> optionalCustomer = customerRepository.findById(key);
        if (optionalCustomer.isPresent()){
            if (employeeRepository.existsByEmployeeCode(request.getFunctionary())){
                Customer oldCustomer = optionalCustomer.get();
                if (oldCustomer.getCustomerStatus().equalsIgnoreCase("A")) {
                    Customer customer = this.modelMapper.map(request, Customer.class);
                    fillNonEditableFields(customer, oldCustomer);
                    customer = customerRepository.save(customer);
                    return modelMapper.map(customer, CustomerResponse.class);
                }else {
                    errors.add(inactiveEntity("cliente"));
                }
            }else{
                errors.add(functionaryNonexistent("creación de cliente"));
            }
        }else{
            errors.add(entityNotFoundForUpdate("cliente"));
        }
        return new CustomerResponse(errors);
    }

    public AbstractResponse deleteCustomer(String type, String code){
        ErrorList errors = new ErrorList();
        PersonKey key = new PersonKey(type, code);
        Optional<Customer> optionalCustomer = customerRepository.findById(key);
        if (optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            if (customer.getCustomerStatus().equalsIgnoreCase("A")){
                BigDecimal balance = accountRepository.sumBalanceByCustomerCode(customer.getCustomerCode());

                if (balance == null || balance.equals(new BigDecimal(0))){
                    if (balance != null) accountRepository.updateAccountByCustomerCode(customer.getCustomerCode());
                    customer.setCustomerStatus("I");
                    customerRepository.save(customer);
                    return new AbstractResponse();
                }else{
                    errors.add(accountsByBalance());
                }
            }else{
                errors.add(inactiveEntity("cliente"));
            }
        }else{
            errors.add(entityNotFound("cliente"));
        }
        return new AbstractResponse(errors);
    }

    public CustomerResponse reactivateCustomer(String type, String code){
        ErrorList errors = new ErrorList();
        PersonKey key = new PersonKey(type, code);
        Optional<Customer> optionalCustomer = customerRepository.findById(key);
        if (optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            if (customer.getCustomerStatus().equalsIgnoreCase("I")){
                customer.setCustomerStatus("A");
                customer = customerRepository.save(customer);
                return modelMapper.map(customer, CustomerResponse.class);
            }else{
                errors.add(activeEntity("cliente"));
            }
        }else{
            errors.add(entityNotFound("cliente"));
        }
        return new CustomerResponse(errors);
    }

}
