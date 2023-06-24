package com.devsu.bankapi.utils.dto.response;

import com.devsu.bankapi.utils.dto.general.PersonRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse extends PersonRequest {

    private Integer employeeCode;

    private Integer currentAgency;

    private Boolean employeeStatus;

    private LocalDateTime creationDate;

}
