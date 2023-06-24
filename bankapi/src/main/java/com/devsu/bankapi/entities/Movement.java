package com.devsu.bankapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movement")
public class Movement {

    @Id
    @Column(name = "movementId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movementId;

    @Column(name = "creationAgency")
    private Integer creationAgency;

    @Column(name = "correlative")
    private Integer correlative;

    @Column(name = "accountType")
    private Integer accountType;

    @Column(name = "applicationDate")
    private LocalDateTime applicationDate;

    /**
     *  true - credito
     *  false - debito
     */
    @Column(name = "movementType")
    private Boolean movementType;

    @Column(name = "movementDescription")
    private String movementDescription;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "reverseMovement")
    private Long reverseMovement;

    /**
     *  A - Activo
     *  R - Reversado
     *  E - Reversa
     */
    @Column(name = "movementStatus")
    private String movementStatus;

    @PrePersist
    private void beforeSave(){
        if (applicationDate == null) applicationDate = LocalDateTime.now();
        if (movementStatus == null) movementStatus = "A";
        if (movementDescription == null) movementDescription = movementType ?
                "Credito de $" + amount : "Debito de $" + amount;
    }

}
