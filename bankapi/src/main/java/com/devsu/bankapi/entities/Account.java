package com.devsu.bankapi.entities;

import com.devsu.bankapi.entities.keys.AccountKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account")
@IdClass(AccountKey.class)
public class Account {

    @Id
    @Column(name = "creationAgency")
    private Integer creationAgency;

    @Id
    @Column(name = "correlative")
    private Integer correlative;

    @Id
    @Column(name = "accountType")
    private Integer accountType;

    @Column(name = "formatedIdentifier")
    private String formatedIdentifier;

    @Column(name = "openingBalance")
    private BigDecimal openingBalance;

    @Column(name = "currentBalance")
    private BigDecimal currentBalance;

    @Column(name = "accountStatus")
    private String accountStatus;

    @Column(name = "openingDate")
    private LocalDateTime openingDate;

    @Column(name = "openingEmployee")
    private Integer functionary;

    @Column(name = "dailyLimit")
    private BigDecimal dailyLimit;

    @Column(name = "currency")
    private String currency;

    @Column(name = "customerCode")
    private Long customerCode;

    @Column(name = "lastUpdateEmployee")
    private Integer lastUpdateEmployee;

    @Column(name = "lastUpdateDate")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    private void beforeSave(){
        if (this.openingDate == null){
            this.openingDate = LocalDateTime.now();
            this.lastUpdateDate = openingDate;
            this.lastUpdateEmployee = functionary;
            this.currentBalance = this.openingBalance;
            this.accountStatus = "A";
            if (dailyLimit == null) this.dailyLimit = new BigDecimal(
                    this.currency.equalsIgnoreCase("QUE") ? 10000 : 1000);
            this.formatedIdentifier = String.format("%3s", this.creationAgency).replace(" ", "0")
                    + "-" + String.format("%7s", this.correlative).replace(" ", "0") +
                    "-" + this.accountType;
        }
    }

    @PreUpdate
    private void beforeUpdate(){
        setLastUpdateDate(LocalDateTime.now());
    }

}
