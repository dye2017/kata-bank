package com.bank.repository;

import com.bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by davidye on 14/06/2017.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a from Account a " +
            "FETCH JOIN a.transfers t " +
            "WHERE a.id = :id ")
    Account findByIdWithTransfer(@Param("id") Long id);

    // Utilisez ce find si on ne veut pas utiliser l'Optimistic Locking par défaut du findOne
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a from Account a " +
            "WHERE a.id = :id ")
    Account findOneWithLock(@Param("id") Long id);

}
