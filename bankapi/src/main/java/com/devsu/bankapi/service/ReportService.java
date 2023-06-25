package com.devsu.bankapi.service;

import com.devsu.bankapi.entities.Account;
import com.devsu.bankapi.entities.Customer;
import com.devsu.bankapi.entities.Movement;
import com.devsu.bankapi.repositories.AccountRepository;
import com.devsu.bankapi.repositories.CustomerRepository;
import com.devsu.bankapi.repositories.MovementRepository;
import com.devsu.bankapi.utils.dto.response.report.AccountReport;
import com.devsu.bankapi.utils.dto.response.report.CustomerReport;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.devsu.bankapi.utils.functions.CommonErrors.*;
import static com.devsu.bankapi.utils.functions.CommonFunctions.stringToLocalDateTime;
import static com.devsu.bankapi.utils.functions.mappers.ReportMappers.accountEntityToAccountReport;
import static com.devsu.bankapi.utils.functions.mappers.ReportMappers.customerEntityToCustomerReport;

@Slf4j
@Service
public class ReportService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final MovementRepository movementRepository;

    public ReportService(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            MovementRepository movementRepository
    ){
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.movementRepository = movementRepository;
    }

    public CustomerReport generateReport(Long customerId, String rangeDates){
        ErrorList errors = new ErrorList();
        String[] dates = rangeDates.split("_");
        if (dates.length == 2){
            LocalDateTime startDate = stringToLocalDateTime(dates[0], errors);
            LocalDateTime endDate = stringToLocalDateTime(dates[1], errors);
            if (errors.isEmpty()){
                if (endDate.isAfter(startDate)){
                    Optional<Customer> optionalCustomer = customerRepository.findByCustomerCode(customerId);
                    if (optionalCustomer.isPresent()){
                        CustomerReport customerReport = customerEntityToCustomerReport(
                                optionalCustomer.get(), startDate, endDate
                        );
                        customerReport.setAccounts(accountsByClient(customerId, startDate, endDate));
                        return customerReport;
                    }else {
                        errors.add(entityNotFound("cliente"));
                    }
                }else {
                    errors.add(startDateAfterEndError());
                }
            }
        }else {
            errors.add(rangeDateError());
        }
        return new CustomerReport(errors);
    }

    private List<AccountReport> accountsByClient(Long customerCode, LocalDateTime start,
                                                 LocalDateTime end){
        List<Account> accounts = accountRepository.findByCustomerCode(customerCode);
        if (!accounts.isEmpty()){
            List<AccountReport> accountsReport = new LinkedList<>();
            for (Account account : accounts){
                List<Movement> accountMovements = movementRepository
                        .findAllByCreationAgencyAndCorrelativeAndAccountTypeAndApplicationDateBetween(
                                account.getCreationAgency(),
                                account.getCorrelative(),
                                account.getAccountType(),
                                start.withHour(0).withMinute(0).withSecond(0),
                                end.withHour(23).withMinute(59).withSecond(59),
                                Sort.by(Sort.Direction.ASC, "applicationDate")
                        );
                if (accountMovements == null) accountMovements = new LinkedList<>();
                AccountReport accountReport = accountEntityToAccountReport(account, accountMovements);
                accountsReport.add(accountReport);
            }
            return accountsReport;
        }
        return new LinkedList<>();
    }

}
