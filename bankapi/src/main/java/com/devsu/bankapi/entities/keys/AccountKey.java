package com.devsu.bankapi.entities.keys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountKey implements Serializable {

    private Integer creationAgency;

    private Integer correlative;

    private Integer accountType;

}
