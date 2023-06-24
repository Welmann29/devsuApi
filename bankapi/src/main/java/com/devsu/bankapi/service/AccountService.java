package com.devsu.bankapi.service;

import com.devsu.bankapi.entities.Account;
import com.devsu.bankapi.entities.Employee;
import com.devsu.bankapi.entities.keys.AccountKey;
import com.devsu.bankapi.repositories.AccountRepository;
import com.devsu.bankapi.repositories.CustomerRepository;
import com.devsu.bankapi.repositories.EmployeeRepository;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.dto.general.PageableResponse;
import com.devsu.bankapi.utils.dto.request.OpenAccountRequest;
import com.devsu.bankapi.utils.dto.request.UpdateAccountRequest;
import com.devsu.bankapi.utils.dto.response.AccountResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.devsu.bankapi.utils.functions.CommonErrors.*;
import static com.devsu.bankapi.utils.functions.CommonFunctions.getAccountKeyByIdentifier;

@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    public  AccountService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            ModelMapper modelMapper
    ){
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public AccountResponse openAccount(OpenAccountRequest request){
        ErrorList errors = new ErrorList();
        Optional<Employee> employeeOptional = employeeRepository.findByEmployeeCode(request.getFunctionary());
        if (employeeOptional.isPresent()){
            if (customerRepository.existsByCustomerCodeAndCustomerStatus(request.getCustomerCode(), "A")){
                Account account = modelMapper.map(request, Account.class);
                account.setCreationAgency(employeeOptional.get().getCurrentAgency());
                Integer correlative = accountRepository.findMaxCorrelative(account.getCreationAgency(),
                        account.getAccountType());
                account.setCorrelative(correlative == null ? 1 : correlative + 1);
                account = accountRepository.save(account);
                return modelMapper.map(account, AccountResponse.class);
            }else{
                errors.add(entityNotFound("cliente"));
            }
        }else {
            errors.add(functionaryNonexistent("aperturar cuenta"));
        }
        return new AccountResponse(errors);
    }

    public PageableResponse accountByCustomer(Long customerCode){
        ErrorList errors = new ErrorList();
        if (customerRepository.existsByCustomerCodeAndCustomerStatus(customerCode, "A")){
            List<Account> accounts = accountRepository.findByCustomerCode(customerCode);
            if (accounts.size() > 0){
                return new PageableResponse(
                        accounts
                                .stream()
                                .map((entity) -> modelMapper.map(entity, AccountResponse.class))
                                .collect(Collectors.toList()),
                        null,
                        (long) accounts.size(),
                        null
                );
            }else {
                return new PageableResponse(
                         new LinkedList<>(),
                        null,
                        (long) 0,
                        null
                );
            }
        }else{
            errors.add(entityNotFound("cliente"));
        }
        return new PageableResponse(errors);
    }

    public PageableResponse allPageable(Integer page, Integer size){
        if (page == 0 && size == 0){
            List<Account> accounts = accountRepository.findAll();
            return new PageableResponse(
                    accounts
                            .stream()
                            .map((entity) -> modelMapper.map(entity, AccountResponse.class))
                            .collect(Collectors.toList()),
                    0,
                    (long) accounts.size(),
                    accounts.size()
            );
        }else{
            Pageable paging = PageRequest.of(page, size);
            Page<Account> accountPaging = accountRepository.findAll(paging);
            return new PageableResponse(
                    accountPaging.getContent()
                            .stream()
                            .map((entity) -> modelMapper.map(entity, AccountResponse.class))
                            .collect(Collectors.toList()),
                    accountPaging.getNumber(),
                    accountPaging.getTotalElements(),
                    accountPaging.getTotalPages()
            );
        }
    }

    public AccountResponse getByIdentifier(String identifier){
        ErrorList errors = new ErrorList();
        if(identifier.matches("^([0-9]{3}-[0-9]{7}-[0-9])")) {
            AccountKey accountKey = getAccountKeyByIdentifier(identifier);
            Optional<Account> accountOptional = accountRepository.findById(accountKey);
            if (accountOptional.isPresent()) {
                return modelMapper.map(accountOptional.get(), AccountResponse.class);
            } else {
                errors.add(entityNotFound("cuenta"));
            }
        }else{
            errors.add(regexValidation("identificador de cuenta"));
        }
        return new AccountResponse(errors);
    }

    public AccountResponse updateAccount(UpdateAccountRequest updateAccountRequest){
        ErrorList errors = new ErrorList();
        AccountKey accountKey = getAccountKeyByIdentifier(updateAccountRequest.getAccountIdentifier());
        Optional<Account> optionalAccount = accountRepository.findById(accountKey);
        if (optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            account.setLastUpdateEmployee(updateAccountRequest.getFunctionary());
            if (updateAccountRequest.getStatus() != null){
                if (validateStatusChange(account, errors, updateAccountRequest.getStatus())) {
                    account.setAccountStatus(updateAccountRequest.getStatus());
                    account = accountRepository.save(account);
                    return modelMapper.map(account, AccountResponse.class);
                }
            }
            if (updateAccountRequest.getDayLimit() != null){
                account.setDailyLimit(updateAccountRequest.getDayLimit());
                account = accountRepository.save(account);
                return modelMapper.map(account, AccountResponse.class);
            }
        }else {
            errors.add(entityNotFound("cuenta"));
        }
        return new AccountResponse(errors);
    }

    public AbstractResponse deleteAccount(String accountIdentifier){
        ErrorList errors = new ErrorList();
        if(accountIdentifier.matches("^([0-9]{3}-[0-9]{7}-[0-9])")) {
            AccountKey accountKey = getAccountKeyByIdentifier(accountIdentifier);
            Optional<Account> accountOptional = accountRepository.findById(accountKey);
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                if (!account.getAccountStatus().equalsIgnoreCase("I")){
                    if (account.getCurrentBalance().equals(new BigDecimal(0))){
                        account.setAccountStatus("I");
                        accountRepository.save(account);
                        return new AbstractResponse();
                    }else{
                        errors.add(accountByBalance());
                    }
                }else{
                    errors.add(inactiveEntity("cuenta"));
                }
            } else {
                errors.add(entityNotFound("cuenta"));
            }
        }else{
            errors.add(regexValidation("identificador de cuenta"));
        }
        return new AbstractResponse(errors);
    }

    private boolean validateStatusChange(Account account, ErrorList errors, String status){
        if (status.equalsIgnoreCase("A")){
            if (account.getAccountStatus().equalsIgnoreCase("A")){
                errors.add(activeEntity("cuenta"));
                return false;
            }
        } else {
            if (!account.getAccountStatus().equalsIgnoreCase("A")){
                errors.add(inactiveEntity("cuenta"));
                return false;
            }
        }
        return true;
    }

}
