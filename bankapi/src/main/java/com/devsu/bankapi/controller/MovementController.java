package com.devsu.bankapi.controller;

import com.devsu.bankapi.service.MovementService;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.dto.request.CreateCustomerRequest;
import com.devsu.bankapi.utils.dto.request.InsertMovement;
import com.devsu.bankapi.utils.dto.response.CustomerResponse;
import com.devsu.bankapi.utils.dto.response.MovementResponse;
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

    @DeleteMapping
    public ResponseEntity<MovementResponse> deleteMovement(
            @RequestParam @Valid Long movementId
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
