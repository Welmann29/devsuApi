package com.devsu.bankapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "agency")
public class Agency {

    @Id
    @Column(name = "agencyId")
    @GeneratedValue
    private Integer agencyId;

    @Column(name = "agencyName")
    private String agencyName;

    @Column(name = "direction")
    private String direction;
}
