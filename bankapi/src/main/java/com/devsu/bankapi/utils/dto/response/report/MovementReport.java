package com.devsu.bankapi.utils.dto.response.report;

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
public class MovementReport {

    private Long movementIdentifier;

    private LocalDateTime applicationDate;

    private String movementType;

    private String movementStatus;

    private String description;

    private BigDecimal amount;

    private BigDecimal balance;

}
