package com.devsu.bankapi.utils.functions;

import com.devsu.bankapi.entities.Customer;
import com.devsu.bankapi.entities.keys.AccountKey;
import com.devsu.bankapi.utils.generics.CommonError;
import com.devsu.bankapi.utils.generics.ErrorList;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonFunctions {

    public static void fillNullsFields(Object bean1, Object bean2) {
        if (bean1 == null || bean2 == null) {
            return;
        }

        Field[] fields1 = bean1.getClass().getDeclaredFields();
        Field[] fields2 = bean2.getClass().getDeclaredFields();
        if (fields1.length != fields2.length) {
            return;
        }
        for (int i = 0; i < fields1.length; i++) {
            Field field1 = fields1[i];
            Field field2 = fields2[i];
            try {
                field1.setAccessible(true);
                if (field1.get(bean1) == null) field1.set(bean1, field2.get(bean2));
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public static void fillNonEditableFields(Customer edit, Customer original){
        edit.setCustomerStatus(original.getCustomerStatus());
        edit.setCreationDate(original.getCreationDate());
        edit.setCustomerCode(original.getCustomerCode());
        edit.setLastUpdateEmployee(edit.getFunctionary());
        edit.setFunctionary(original.getFunctionary());
    }

    public static AccountKey getAccountKeyByIdentifier(String accountIdentifier){
        String[] accountPart = accountIdentifier.split("-");
        return new AccountKey(
                Integer.valueOf(accountPart[0]),
                Integer.valueOf(accountPart[1]),
                Integer.valueOf(accountPart[2])
        );
    }

    public static LocalDateTime stringToLocalDateTime(String date, ErrorList errors){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            return LocalDate.parse(date, formatter).atStartOfDay();
        }catch (Exception e){
            errors.add(new CommonError(
                114,
                    "El formato de la fecha es erroneo",
                    LocalDateTime.now()
            ));
        }
        return LocalDateTime.now();
    }

}
