package com.devsu.bankapi.utils.dto.response;

import com.devsu.bankapi.utils.dto.general.PersonResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse extends PersonResponse {

    private Long customerCode;

    private String customerStatus;

    private Integer functionary;

    private LocalDateTime creationDate;

    private Integer lastUpdateEmployee;

    private LocalDateTime lastUpdateDate;

    public CustomerResponse(ErrorList errors){
        super(errors);
    }

}
