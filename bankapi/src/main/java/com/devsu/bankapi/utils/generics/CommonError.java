package com.devsu.bankapi.utils.generics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CommonError {

    /**
     *  101 - Funcionario invalido
     *  102 - codigo existente
     *  103 - Validacion de campo
     *  104 - No existe registro
     *  105 - No existe registro para hacer update
     *  106 - Existe registro inactivo
     *  107 - cuentas con balance
     *  108 - activo
     *  109 - cuenta con balance
     *  110 - Estado de cuenta no permite movimientos
     *  111 - Fondos insuficientes para realizar el debito
     *  112 - Se supero el limite diario de retiro
     *  400 - Error en el consumo de la base de datos
     */
    private int errorCode;

    private String description;

    private LocalDateTime timeOccurred;

}
