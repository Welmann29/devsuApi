package com.devsu.bankapi.utils.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InsertMovement {

    @NotBlank(message = "Es necesario que envies el identificador de la cuenta")
    @Pattern(regexp = "^([0-9]{3}-[0-9]{7}-[0-9])", message = "El identificador no tiene el formato correcto")
    private String accountIdentifier;

    private Integer creationAgency;

    private Integer correlative;

    private Integer accountType;

    private LocalDateTime applicationDate;

    /**
     *  true - credito
     *  false - debito
     */
    @NotNull(message = "Se requiere conocer el tipo de movimiento")
    private Boolean movementType;

    @Size(max = 255, message = "La descripción no puede tener mas de 255 caracteres")
    private String movementDescription;

    @Digits(integer = 13, fraction = 2, message = "El valor debe cumplir con el formato (13, 2)")
    @Positive(message = "El valor debe ser positivo")
    private BigDecimal amount;

}
