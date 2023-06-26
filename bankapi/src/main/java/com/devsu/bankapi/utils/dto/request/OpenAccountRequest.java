package com.devsu.bankapi.utils.dto.request;

import com.devsu.bankapi.utils.abstracts.AdministrativeAbstractRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OpenAccountRequest extends AdministrativeAbstractRequest {

    @NotNull(message = "Es requerido conocer el tipo de cuenta")
    @Min(value = 1, message = "Solo se aceptan los valores (1 - Cuenta de ahorro) y (2 - Cuenta monetaria)")
    @Max(value = 2, message = "Solo se aceptan los valores (1 - Cuenta de ahorro) y (2 - Cuenta monetaria)")
    @Schema(description = "1 - Cuenta de ahorro, 2 = Cuenta monetaria")
    private Integer accountType;

    @NotNull(message = "Es requerido conocer el saldo de apertura")
    @Digits(integer = 13, fraction = 2, message = "El valor debe cumplir con el formato (13, 2)")
    @Positive(message = "El valor debe ser positivo")
    private BigDecimal openingBalance;

    @Digits(integer = 13, fraction = 2, message = "El valor debe cumplir con el formato (13, 2)")
    @Positive(message = "El valor debe ser positivo")
    @DecimalMax(value = "1000.00", message = "El limite diario no puede ser mayor a $1000")
    private BigDecimal dailyLimit;

    @NotNull(message = "Es obligatorio conocer el codigo del cliente al que se le aperturara la cuenta")
    @Min(message = "Los codigos son mayores a 0", value = 1)
    private Long customerCode;

    @NotNull(message = "Es necesario conocer la moneda")
    @Pattern(regexp = "^(DOL)", message = "El tipo ingresado no es correcto")
    private String currency;

}
