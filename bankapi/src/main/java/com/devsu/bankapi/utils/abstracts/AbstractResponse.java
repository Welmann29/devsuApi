package com.devsu.bankapi.utils.abstracts;

import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AbstractResponse implements Serializable {

    private Boolean success;

    private ErrorList errors;

    public AbstractResponse() {
        this.success = true;
    }

    public AbstractResponse(ErrorList errors) {
        this.success = false;
        this.errors = errors;
    }

}
