package com.devsu.bankapi.utils.dto.general;

import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse extends AbstractResponse {

    private String documentType;

    private String document;

    private String firstName;

    private String middleName;

    private String firstSurname;

    private String secondSurname;

    private String gender;

    private String direction;

    private String internationalCallingCode;

    private String phoneNumber;

    public PersonResponse(ErrorList errors){
        super(errors);
    }

}
