package com.devsu.bankapi.controller;

import com.devsu.bankapi.service.AccountService;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.dto.general.PageableResponse;
import com.devsu.bankapi.utils.dto.request.OpenAccountRequest;
import com.devsu.bankapi.utils.dto.request.UpdateAccountRequest;
import com.devsu.bankapi.utils.dto.response.AccountResponse;
import com.devsu.bankapi.utils.functions.decorators.LogDevsu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @LogDevsu
    @Operation(summary = "Creación de cuentas monetarias y de ahorros para clientes registrados", description =
            "Es requerido que ingrese el funcionario que realizara la acción")
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

    @LogDevsu
    @Operation(summary = "Obtención por paginado o total de las cuentas registradas",
                description = "Si se desean obtener todas las cuentas existentes enviar 0 en ambos campos")
    @GetMapping
    public ResponseEntity<PageableResponse> accountPaging(
            @Parameter(description = "Numero de pagina deseada (Enviar 0 en ambos campos para obtener todas)")
            @RequestParam Integer page,
            @Parameter(description = "Cantidad de elementos deseados (Enviar 0 en ambos campos para obtener todas)")
            @RequestParam Integer size
    ){
        PageableResponse res = accountService.allPageable(page, size);
        if (res.getSuccess()){
            return ResponseEntity.ok().body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @LogDevsu
    @Operation(summary = "Cuentas asociadas a un cliente especifico")
    @GetMapping("/byCustomerCode")
    public ResponseEntity<PageableResponse> accountsByCustomerCode(
            @Parameter(description = "Id del cliente interno") @RequestParam Long customerCode
    ){
        PageableResponse res = accountService.accountByCustomer(customerCode);
        if (res.getSuccess()){
            return ResponseEntity.ok().body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @LogDevsu
    @Operation(summary = "Información de la cuenta ingresada")
    @GetMapping("/byAccountIdentifier")
    public ResponseEntity<AccountResponse> accountByIdentifier(
            @Parameter(description = "Identificador de la cuenta formato NNN-NNNNNNN-N",
                    example = "001-0000003-2") @RequestParam String identifier
    ){
        AccountResponse res = accountService.getByIdentifier(identifier);
        if (res.getSuccess()){
            return ResponseEntity.ok().body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @LogDevsu
    @Operation(summary = "Actualización de ciertos parametors de la cuenta", description =
                "Solo se permite la actualización del limite diario y el estado de la cuenta, los cuales pueden ser " +
                        "E - Embargada, R - En Revisión, A - Para reactivarala, si una cuenta se embarga o se pone en " +
                        "revisión la misma ya no admitira movimientos, se requiere conocer el funcionario que " +
                        "realiza la acción")
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

    @LogDevsu
    @Operation(summary = "Desactivación de una cuenta activa", description =
            "Para desactivar una cuenta es necesario que la misma tenga un balance de 0.00, si el balance " +
                    "es diferente de 0.00, registre un movimiento para retirar ese dinero, una vez desactivada " +
                    "la cuenta no admitira movimientos")
    @DeleteMapping
    public ResponseEntity<AbstractResponse> deleteAccount(
            @Parameter(description = "Identificador de la cuenta formato NNN-NNNNNNN-N",
                    example = "001-0000003-2") @RequestParam String accountIdentifier
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
