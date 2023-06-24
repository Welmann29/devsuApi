package com.devsu.bankapi.utils.functions;

import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.generics.CommonError;
import com.devsu.bankapi.utils.generics.ErrorList;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CommonErrors {

    public static CommonError functionaryNonexistent(String accion){
        return new CommonError(
                101,
                "El funcionario ingresado para " + accion
                        + " no existe, revise el codigo ingresado",
                LocalDateTime.now()
        );
    }

    public static CommonError databaseError(String accion, Exception e){
        return new CommonError(
                400,
                "Ocurrió un error al intentar " + accion + " > " + e.getMessage(),
                LocalDateTime.now()
        );
    }

    public static CommonError duplicateEntityByKey(String entity){
        return new CommonError(
                102,
                "Ya existe un registro de " + entity + " con la llave primaria proporcionada",
                LocalDateTime.now()
        );
    }

    public static CommonError inactiveEntity(String entity){
        return new CommonError(
                106,
                "Ya existe un registro de " + entity + " con la llave primaria proporcionada, pero se " +
                        "encuentra inactivo, realice las gestiones para reactivarlo",
                LocalDateTime.now()
        );
    }

    public static CommonError activeEntity(String entity){
        return new CommonError(
                108,
                "Ya existe un registro de " + entity + " activo con la llave primaria proporcionada",
                LocalDateTime.now()
        );
    }

    public static CommonError entityNotFound(String entity){
        return new CommonError(
                104,
                "No existe un registro de " + entity + " con la llave proporcionada",
                LocalDateTime.now()
        );
    }

    public static CommonError entityNotFoundForUpdate(String entity){
        return new CommonError(
                105,
                "No existe un registro de " + entity + " con la llave proporcionada, para su actualizacion, "
                        +  "por favor, creelo",
                LocalDateTime.now()
        );
    }

    public static CommonError accountsByBalance(){
        return new CommonError(
                107,
                "Las cuentas del cliente deben tener un balance de 0 para poder desactivarlo",
                LocalDateTime.now()
        );
    }

    public static CommonError accountByBalance(){
        return new CommonError(
                108,
                "Las cuenta no puede ser desctivada pues su balance no es igual a 0",
                LocalDateTime.now()
        );
    }

    public static CommonError accountNotActive(String status, String accountIdentifier){
        String message = "La cuenta " + accountIdentifier + " no puede recibir movimientos por que se encuentra " +
                switch (status) {
                    case "E" -> "embargada, se debe contactar con el cliente";
                    case "R" -> "en revisión, pronto tendra una resolución";
                    case "I" -> "inactiva, se deben realizar gestiones administrativas";
                            default -> "en un estado inconcluso, requiere revision";
                };
        return new CommonError(
                110,
                message,
                LocalDateTime.now()
        );
    }

    public static CommonError insufficientFunds(){
        return new CommonError(
                111,
                "Los fondos de la cuenta son insuficientes para realizar el debito",
                LocalDateTime.now()
        );
    }

    public static CommonError overLimit(BigDecimal dailyLimit, BigDecimal overdraft){
        return new CommonError(
                112,
                "Sobrepasa la cantidad posible a debitar en el dia ($" + dailyLimit
                        + ") con este movimiento llegaria a $" + overdraft + ", intentelo el dia de mañana",
                LocalDateTime.now()
        );
    }

    public static AbstractResponse validationException(MethodArgumentNotValidException ex) {
        ErrorList errors = new ErrorList();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(
                    new CommonError(
                            103,
                            errorMessage + ", revise el campo " + fieldName,
                            LocalDateTime.now()
                    )
            );
        });
        return new AbstractResponse(errors);
    }

    public static CommonError regexValidation(String field){
        return new CommonError(
                103,
                "El valor ingresado non coincide con el formato esperado, revise el campo " + field,
                LocalDateTime.now()
        );
    }
}
