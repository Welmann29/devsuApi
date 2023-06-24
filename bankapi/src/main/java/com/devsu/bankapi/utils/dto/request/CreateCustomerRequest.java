package com.devsu.bankapi.utils.dto.request;

import com.devsu.bankapi.utils.dto.general.PersonRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest extends PersonRequest {

    @NotNull(message = "La contraseña es requerida")
    private String passwordCustomer;

}
