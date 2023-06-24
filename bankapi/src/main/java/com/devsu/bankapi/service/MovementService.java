package com.devsu.bankapi.service;

import com.devsu.bankapi.entities.Account;
import com.devsu.bankapi.entities.Movement;
import com.devsu.bankapi.entities.keys.AccountKey;
import com.devsu.bankapi.repositories.AccountRepository;
import com.devsu.bankapi.repositories.MovementRepository;
import com.devsu.bankapi.utils.dto.request.InsertMovement;
import com.devsu.bankapi.utils.dto.response.MovementResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.devsu.bankapi.utils.functions.CommonErrors.*;
import static com.devsu.bankapi.utils.functions.CommonFunctions.getAccountKeyByIdentifier;

@Slf4j
@Service
public class MovementService {

    private final AccountRepository accountRepository;

    private final MovementRepository movementRepository;

    private final ModelMapper modelMapper;

    public MovementService(
            AccountRepository accountRepository,
            MovementRepository movementRepository,
            ModelMapper modelMapper
    ){
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
        this.modelMapper = modelMapper;
    }

    public MovementResponse insertMovement(InsertMovement movement){
        ErrorList errors = new ErrorList();
        AccountKey accountKey = getAccountKeyByIdentifier(movement.getAccountIdentifier());
        Optional<Account> optionalAccount = accountRepository.findById(accountKey);
        if (optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            if (account.getAccountStatus().equalsIgnoreCase("A")){
                BigDecimal balanceAfterMovement = account.getCurrentBalance();
                if (movement.getMovementType()){
                    balanceAfterMovement = balanceAfterMovement.add(movement.getAmount());
                } else {
                    balanceAfterMovement = balanceAfterMovement.subtract(movement.getAmount());
                    if (balanceAfterMovement.compareTo(new BigDecimal(0)) < 0){
                        errors.add(insufficientFunds());
                    } else {
                        BigDecimal amountDebitedForDay = movementRepository.amountDebitedForDay(
                                movement.getApplicationDate() == null ?
                                        LocalDateTime.now().withHour(0).withMinute(0).withSecond(0) :
                                        movement.getApplicationDate().withHour(0).withMinute(0).withSecond(0),
                                movement.getApplicationDate() == null ?
                                        LocalDateTime.now().withHour(23).withMinute(59).withSecond(59) :
                                        movement.getApplicationDate().withHour(23).withMinute(59).withSecond(59),
                                accountKey.getCreationAgency(), account.getCorrelative(), account.getAccountType()
                        );
                        if (amountDebitedForDay == null) amountDebitedForDay = new BigDecimal(0);
                        amountDebitedForDay = amountDebitedForDay.add(movement.getAmount());
                        if (amountDebitedForDay.compareTo(account.getDailyLimit()) > 0) {
                            errors.add(overLimit(account.getDailyLimit(), amountDebitedForDay));
                        }
                    }
                }
                if (errors.isEmpty()) {
                    account.setCurrentBalance(balanceAfterMovement);
                    Movement movementToInsert = modelMapper.map(movement, Movement.class);
                    movementToInsert.setCreationAgency(accountKey.getCreationAgency());
                    movementToInsert.setCorrelative(account.getCorrelative());
                    movementToInsert.setAccountType(accountKey.getAccountType());
                    movementToInsert.setBalance(balanceAfterMovement);
                    movementToInsert = movementRepository.save(movementToInsert);
                    accountRepository.save(account);
                    return modelMapper.map(movementToInsert, MovementResponse.class);
                }
            }else{
                errors.add(accountNotActive(account.getAccountStatus(), movement.getAccountIdentifier()));
            }
        }else {
            errors.add(entityNotFound("cuenta"));
        }
        return new MovementResponse(errors);
    }

    public MovementResponse reverseMovement(Long movementId){
        ErrorList errors = new ErrorList();
        Optional<Movement> optionalMovement = movementRepository.findById(movementId);
        if (optionalMovement.isPresent()){
            Movement movement = optionalMovement.get();
            Account account = accountRepository.getReferenceById(new AccountKey(
                    movement.getCreationAgency(),
                    movement.getCorrelative(),
                    movement.getAccountType()
            ));
            if (account.getAccountStatus().equalsIgnoreCase("A")) {
                if (movement.getMovementStatus().equalsIgnoreCase("A")) {
                    movement.setMovementStatus("R");
                    if (!movement.getMovementType() || account.getCurrentBalance().subtract(movement.getAmount())
                            .compareTo(new BigDecimal(0)) > 0){
                        Movement reverse = getReversedMovement(movement, account.getCurrentBalance());
                        account.setCurrentBalance(reverse.getBalance());
                        movementRepository.save(movement);
                        reverse = movementRepository.save(reverse);
                        accountRepository.save(account);
                        return modelMapper.map(reverse, MovementResponse.class);
                    }else{
                        errors.add(insufficientFunds());
                    }
                } else {
                    errors.add(inactiveEntity("movimiento"));
                }
            }else{
                errors.add(inactiveEntity("cuenta"));
            }
        }else{
            errors.add(entityNotFound("movimiento"));
        }
        return new MovementResponse(errors);
    }

    private Movement getReversedMovement(Movement movement, BigDecimal balance){
        return new Movement(
                null,
                movement.getCreationAgency(),
                movement.getCorrelative(),
                movement.getAccountType(),
                null,
                !movement.getMovementType(),
                "Reversa del movimiento " + movement.getMovementId() + " luego de analisis administrativo",
                movement.getAmount(),
                !movement.getMovementType() ? balance.add(movement.getAmount())
                        : balance.subtract(movement.getAmount()),
                movement.getMovementId(),
                "E"
        );
    }

}
