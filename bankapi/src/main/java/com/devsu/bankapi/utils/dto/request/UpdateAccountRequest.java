package com.devsu.bankapi.utils.dto.request;

import com.devsu.bankapi.utils.abstracts.AdministrativeAbstractRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateAccountRequest extends AdministrativeAbstractRequest {

    @NotBlank(message = "Es necesario que envies el identificador de la cuenta")
    @Pattern(regexp = "^([0-9]{3}-[0-9]{7}-[0-9])", message = "El identificador no tiene el formato correcto")
    private String accountIdentifier;

    @Digits(integer = 13, fraction = 2, message = "El valor debe cumplir con el formato (13, 2)")
    @Positive(message = "El valor debe ser positivo")
    @DecimalMax(value = "1000.00", message = "El limite diario no puede ser mayor a $1000")
    private BigDecimal dayLimit;

    @Pattern(regexp = "^(E|R|A)", message = "Los unicos estados que le puedes asignar a la cuenta son (E. Embargada), " +
            "(R. Revision) o reactivarla en caso no lo este")
    private String status;

}
