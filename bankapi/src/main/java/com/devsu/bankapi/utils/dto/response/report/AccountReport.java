package com.devsu.bankapi.utils.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountReport {

    private String accountIdentifier;

    private String accountType;

    private String currency;

    private String accountStatus;

    private LocalDate  openingDate;

    private BigDecimal openingBalance;

    private BigDecimal currentBalance;

    private BigDecimal initialBalancePeriod;

    private BigDecimal endingBalancePeriod;

    private Integer totalCreditsPeriod;

    private Integer totalDebitPeriod;

    private List<MovementReport> movements;

}
