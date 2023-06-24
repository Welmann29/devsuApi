package com.devsu.bankapi.repositories;

import com.devsu.bankapi.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface MovementRepository extends
        JpaRepository<Movement, Long>,
        JpaSpecificationExecutor<Movement> {

    @Query("SELECT SUM(amount) FROM Movement where movementType=false " +
            "and applicationDate >= :start and applicationDate <= :finish and " +
            "creationAgency = :creationAgency and correlative = :correlative and accountType = :accountType")
    BigDecimal amountDebitedForDay(
            @Param("start") LocalDateTime start,
            @Param("finish") LocalDateTime finish,
            @Param("creationAgency") Integer creationAgency,
            @Param("correlative") Integer correlative,
            @Param("accountType") Integer accountType
    );

}
