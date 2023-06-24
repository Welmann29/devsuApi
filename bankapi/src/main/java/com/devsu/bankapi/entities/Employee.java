package com.devsu.bankapi.entities;

import com.devsu.bankapi.entities.keys.PersonKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "employee")
@IdClass(PersonKey.class)
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Person {

    @Column(name = "employeeCode")
    @GeneratedValue
    private Integer employeeCode;

    @Column(name = "currentAgency")
    private Integer currentAgency;

    @Column(name = "employeeStatus")
    private boolean employeeStatus;

    @Column(name = "creationDate")
    private LocalDateTime creationDate;

    @PrePersist
    private void setCreationDate(){
        if (this.creationDate == null){
            this.creationDate = LocalDateTime.now();
        }
    }

}
