package com.devsu.bankapi.utils.dto.response;

import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import jakarta.persistence.Column;
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
public class AccountResponse extends AbstractResponse {

    private Integer creationAgency;

    private Integer correlative;

    private Integer accountType;

    private String formatedIdentifier;

    private BigDecimal openingBalance;

    private BigDecimal currentBalance;

    private String accountStatus;

    private LocalDateTime openingDate;

    private Integer functionary;

    private BigDecimal dailyLimit;

    private String currency;

    private Long customerCode;

    private Integer lastUpdateEmployee;

    private LocalDateTime lastUpdateDate;

    public AccountResponse(ErrorList errors ) { super(errors); }

}
