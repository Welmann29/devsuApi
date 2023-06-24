package com.devsu.bankapi.controller;

import com.devsu.bankapi.service.AccountService;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.dto.general.PageableResponse;
import com.devsu.bankapi.utils.dto.request.OpenAccountRequest;
import com.devsu.bankapi.utils.dto.request.UpdateAccountRequest;
import com.devsu.bankapi.utils.dto.response.AccountResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static com.devsu.bankapi.utils.functions.CommonErrors.validationException;

@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(
            AccountService accountService
    ){
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody @Valid OpenAccountRequest request
    ){
        AccountResponse res = accountService.openAccount(request);
        if (res.getSuccess()){
            return ResponseEntity.status(201).body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @GetMapping
    public ResponseEntity<PageableResponse> accountPaging(
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        PageableResponse res = accountService.allPageable(page, size);
        if (res.getSuccess()){
            return ResponseEntity.ok().body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @GetMapping("/byCustomerCode")
    public ResponseEntity<PageableResponse> accountsByCustomerCode(
            @RequestParam Long customerCode
    ){
        PageableResponse res = accountService.accountByCustomer(customerCode);
        if (res.getSuccess()){
            return ResponseEntity.ok().body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @GetMapping("/byAccountIdentifier")
    public ResponseEntity<AccountResponse> accountByIdentifier(
            @RequestParam String identifier
    ){
        AccountResponse res = accountService.getByIdentifier(identifier);
        if (res.getSuccess()){
            return ResponseEntity.ok().body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PatchMapping
    public ResponseEntity<AccountResponse> updateAccount(
            @RequestBody @Valid UpdateAccountRequest request
    ){
        AccountResponse res = accountService.updateAccount(request);
        if (res.getSuccess()){
            return ResponseEntity.ok().body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @DeleteMapping
    public ResponseEntity<AbstractResponse> deleteAccount(
        @RequestParam String accountIdentifier
    ){
        AbstractResponse res = accountService.deleteAccount(accountIdentifier);
        if (res.getSuccess()){
            return ResponseEntity.ok().body(res);
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
