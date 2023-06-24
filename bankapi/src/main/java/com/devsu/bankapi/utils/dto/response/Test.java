package com.devsu.bankapi.utils.dto.response;

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
public class Test extends AbstractResponse {

    private String test;

    public Test(ErrorList errors) {
        super(errors);
    }

}
