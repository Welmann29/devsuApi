package com.devsu.bankapi.utils.dto.request;

import com.devsu.bankapi.utils.dto.general.PersonRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateEmployeeRequest extends PersonRequest {

    private Integer currentAgency;

}
