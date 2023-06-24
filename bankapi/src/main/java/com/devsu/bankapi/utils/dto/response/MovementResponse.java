package com.devsu.bankapi.utils.dto.response;

import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovementResponse extends AbstractResponse {

    private Long movementId;

    private Integer creationAgency;

    private Integer correlative;

    private Integer accountType;

    private LocalDateTime applicationDate;

    /**
     *  true - credito
     *  false - debito
     */
    private Boolean movementType;

    private String movementDescription;

    private BigDecimal amount;

    private BigDecimal balance;

    private long reverseMovement;

    /**
     *  A - Activo
     *  R - Reversado
     *  E - Reversa
     */
    private String movementStatus;

    public MovementResponse(ErrorList errors){ super(errors);}

}
