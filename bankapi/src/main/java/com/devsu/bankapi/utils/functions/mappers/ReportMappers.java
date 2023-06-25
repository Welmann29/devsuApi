package com.devsu.bankapi.utils.functions.mappers;

import com.devsu.bankapi.entities.Account;
import com.devsu.bankapi.entities.Customer;
import com.devsu.bankapi.entities.Movement;
import com.devsu.bankapi.utils.dto.response.report.AccountReport;
import com.devsu.bankapi.utils.dto.response.report.CustomerReport;
import com.devsu.bankapi.utils.dto.response.report.MovementReport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportMappers {

    public static CustomerReport customerEntityToCustomerReport(Customer customer, LocalDateTime start,
                                                         LocalDateTime end){
        return new CustomerReport(
                documentTypeMapper(customer.getDocumentType()),
                customer.getDocument(),
                makeFullName(customer),
                makeCellphone(customer),
                customer.getDirection(),
                customer.getCustomerStatus().equalsIgnoreCase("A") ? "Activo" : "Inactivo",
                start.toLocalDate(),
                end.toLocalDate(),
                null
        );
    }

    private static String documentTypeMapper(String documentType){
        return switch (documentType) {
            case ("D") -> "Documento Personal de Identificación (DPI)";
            case ("P") -> "Pasaporte (Extranjeros)";
            default -> "NaN";
        };
    }

    private static String makeFullName(Customer customer){
        StringBuilder fullName = new StringBuilder();
        fullName.append(customer.getFirstName()).append(" ");
        if (customer.getMiddleName() != null) fullName.append(customer.getMiddleName()).append(" ");
        fullName.append(customer.getFirstSurname());
        if (customer.getSecondSurname() != null) fullName.append(" ").append(customer.getSecondSurname());
        return fullName.toString();
    }

    private static String makeCellphone(Customer customer){
        return customer.getInternationalCallingCode() != null ?
                customer.getInternationalCallingCode() + " " + customer.getPhoneNumber() :
                customer.getPhoneNumber();
    }

    public static AccountReport accountEntityToAccountReport(Account account, List<Movement> movements){
        Integer[] counts = getCountDebitAndCredits(movements);
        return new AccountReport(
                account.getFormatedIdentifier(),
                accountTypeMapper(account.getAccountType()),
                currencyMapper(account.getCurrency()),
                accountStatusMapper(account.getAccountStatus()),
                account.getOpeningDate().toLocalDate(),
                account.getOpeningBalance(),
                account.getCurrentBalance(),
                movements.isEmpty() ? null : getInitialBalanceByMovement(movements.get(0)),
                movements.isEmpty() ? null : movements.get(movements.size() - 1).getBalance(),
                counts[0],
                counts[1],
                movements.isEmpty() ? new LinkedList<>() : movements
                        .stream()
                        .map(ReportMappers::movementEntityToMovementReport)
                        .collect(Collectors.toList())
        );
    }

    private static String accountTypeMapper(Integer accountType){
        return switch (accountType) {
            case (1) -> "Cuenta de ahorro";
            case (2) -> "Cuenta monetaria";
            default -> "NaN";
        };
    }

    private static String currencyMapper(String currency){
        return switch (currency) {
            case ("DOL") -> "Dolares ($)";
            case ("QUE") -> "Quetzales (Q)";
            default -> "NaN";
        };
    }

    private static String accountStatusMapper(String accountStatus){
        return switch (accountStatus) {
            case ("A") -> "Activa";
            case ("E") -> "Embargada";
            case ("R") -> "En Revisión";
            case ("I") -> "Desactivada";
            default -> "NaN";
        };
    }

    private static BigDecimal getInitialBalanceByMovement(Movement movement){
        // Si es un credito se debe restar para rstaurar el balance, si es debito se suma
        return movement.getMovementType() ?
                movement.getBalance().subtract(movement.getAmount()) :
                movement.getBalance().add(movement.getAmount());
    }

    private static Integer[] getCountDebitAndCredits(List<Movement> movements){
        Integer[] counts = {0, 0}; // Posicion 0 para creditos y 1 para debitos
        for (Movement movement : movements){
            if (movement.getMovementType()){
                counts[0]++;
            }else{
                counts[1]++;
            }
        }
        return counts;
    }

    private static MovementReport movementEntityToMovementReport(Movement movement){
        return new MovementReport(
                movement.getMovementId(),
                movement.getApplicationDate(),
                movement.getMovementType() ? "Credito" : "Debito",
                movementStatusMapper(movement.getMovementStatus()),
                movement.getMovementDescription(),
                movement.getMovementType() ? movement.getAmount() :
                        movement.getAmount().multiply(new BigDecimal(-1)),
                movement.getBalance()
        );
    }

    private static String movementStatusMapper(String movementStatus){
        return switch (movementStatus) {
            case ("A") -> "Valido";
            case ("R") -> "Reversado";
            case ("E") -> "Reversa";
            default -> "NaN";
        };
    }

}
