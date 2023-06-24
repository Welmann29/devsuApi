package com.devsu.bankapi.entities;

import com.devsu.bankapi.entities.keys.PersonKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "person")
@IdClass(PersonKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Person implements Serializable {

    @Id
    @Column(name = "documentType")
    private String documentType;

    @Id
    @Column(name = "document")
    private String document;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "firstSurname")
    private String firstSurname;

    @Column(name = "secondSurname")
    private String secondSurname;

    @Column(name = "gender")
    private String gender;

    @Column(name = "direction")
    private String direction;

    @Column(name = "internationaCallingCode")
    private String internationalCallingCode;

    @Column(name = "phoneNumber")
    private String phoneNumber;

}
