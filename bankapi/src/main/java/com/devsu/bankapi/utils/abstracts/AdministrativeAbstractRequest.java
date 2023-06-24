package com.devsu.bankapi.utils.abstracts;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Se utiliza para obligar a escribir un funcionario para
 * los request administrativos (Interacciones de empleados, clientes y cuentas)
 */
@Getter
@Setter
public class AdministrativeAbstractRequest {

    @NotNull(message = "Es requerido conocer el codigo del empleado que realiza la acción")
    private Integer functionary;

}
