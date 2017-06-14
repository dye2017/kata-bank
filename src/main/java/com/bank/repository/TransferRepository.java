package com.bank.repository;

import com.bank.domain.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by davidye on 14/06/2017.
 */
public interface TransferRepository extends JpaRepository<Transfer, Long> {


}
