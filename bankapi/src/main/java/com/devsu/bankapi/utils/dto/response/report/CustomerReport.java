package com.devsu.bankapi.utils.dto.response.report;

import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReport extends AbstractResponse {

    private String documentType;

    private String document;

    private String fullName;

    private String cellphone;

    private String direction;

    private String status;

    private LocalDate startDateReport;

    private LocalDate endDateReport;

    private List<AccountReport> accounts;

    public CustomerReport(ErrorList errors) {super(errors);}

}
