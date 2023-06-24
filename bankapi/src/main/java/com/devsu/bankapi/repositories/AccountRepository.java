package com.devsu.bankapi.repositories;

import com.devsu.bankapi.entities.Account;
import com.devsu.bankapi.entities.keys.AccountKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends
        JpaRepository<Account, AccountKey>,
        JpaSpecificationExecutor<Account> {

    @Query("select SUM(currentBalance) FROM Account where customerCode = :customerCode")
    BigDecimal sumBalanceByCustomerCode(@Param("customerCode") Long customerCode);

    @Modifying
            @Query("UPDATE Account set accountStatus = 'I' where customerCode = :customerCode")
    void updateAccountByCustomerCode(@Param("customerCode") Long customerCode);

    @Query("SELECT MAX(correlative) from Account where creationAgency = :creationAgency " +
            "and accountType = :accountType")
    Integer findMaxCorrelative(
            @Param("creationAgency") Integer creationAgency,
            @Param("accountType") Integer accountType
    );

    List<Account> findByCustomerCode(Long customerCode);

}
