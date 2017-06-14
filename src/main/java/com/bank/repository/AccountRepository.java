package com.bank.repository;

import com.bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by davidye on 14/06/2017.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a from Account a " +
            "FETCH JOIN a.transfers t " +
            "WHERE a.id = :id ")
    Account findByIdWithTransfer(@Param("id") Long id);

}
