package com.devsu.bankapi.controller;

import com.devsu.bankapi.service.MovementService;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.dto.request.CreateCustomerRequest;
import com.devsu.bankapi.utils.dto.request.InsertMovement;
import com.devsu.bankapi.utils.dto.response.CustomerResponse;
import com.devsu.bankapi.utils.dto.response.MovementResponse;
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
@RequestMapping("/api/movement")
public class MovementController {

    private final MovementService movementService;

    public MovementController(
            MovementService movementService
    ){
        this.movementService = movementService;
    }

    @Operation(summary = "Inserción de un movimiento", description = "La cuenta no puede tener un estado diferente a " +
            "A (activa) para poder registrar el movimiento")
    @PostMapping
    public ResponseEntity<MovementResponse> insertMovement(
            @RequestBody @Valid InsertMovement request
    ){
        MovementResponse res = movementService.insertMovement(request);
        if (res.getSuccess()){
            return ResponseEntity.status(201).body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @Operation(summary = "Reversado de un movimiento", description = "Por medio de este metodo se hara el reversado de " +
            "un movimiento, en caso el movimiento sea un credito la cantidad se debitara y si es un debito se " +
            "acreditara de manera automaticca")
    @DeleteMapping
    public ResponseEntity<MovementResponse> deleteMovement(
            @Parameter(description = "Numero de movimiento a reversar") @RequestParam @Valid Long movementId
    ){
        MovementResponse res = movementService.reverseMovement(movementId);
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
