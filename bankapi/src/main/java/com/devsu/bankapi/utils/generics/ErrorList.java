package com.devsu.bankapi.utils.generics;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j
@Getter
@Setter
public class ErrorList extends LinkedList<CommonError> {

    @Override
    public boolean add(CommonError error){
        log.error(error.getDescription());
        return super.add(error);
    }

}
