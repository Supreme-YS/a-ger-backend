package com.ireland.ager.account.repository;

import com.ireland.ager.account.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {


    Optional<Account> findAccountByAccessToken(String accountId);

    Boolean existsAccountByAccountEmail(String accountEmail);
    Optional<Account> findAccountByAccountEmail(String accountEmail);

}