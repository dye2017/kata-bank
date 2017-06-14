package com.bank.repository;

import com.bank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by davidye on 14/06/2017.
 */
public interface UsrRepository extends JpaRepository<User, Long> {


}
