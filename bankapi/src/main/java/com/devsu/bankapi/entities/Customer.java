package com.devsu.bankapi.entities;

import com.devsu.bankapi.entities.keys.PersonKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
@IdClass(PersonKey.class)
public class Customer extends Person {

    @Column(name = "customerCode")
    @GeneratedValue
    private Long customerCode;

    @Column(name = "passwordCustomer")
    private String passwordCustomer;

    @Column(name = "customerStatus")
    private String customerStatus;

    @Column(name = "creationEmployee")
    private Integer functionary;

    @Column(name = "creationDate")
    private LocalDateTime creationDate;

    @Column(name = "lastUpdateEmployee")
    private Integer lastUpdateEmployee;

    @Column(name = "lastUpdateDate")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    private void setCreationDate(){
        if (creationDate == null){
            setCustomerCode(null);
            setCreationDate(LocalDateTime.now());
            setLastUpdateDate(this.creationDate);
            setLastUpdateEmployee(this.functionary);
            setCustomerStatus("A");
        }
    }

    @PreUpdate
    private void beforeUpdate(){
        setLastUpdateDate(LocalDateTime.now());
    }

}
