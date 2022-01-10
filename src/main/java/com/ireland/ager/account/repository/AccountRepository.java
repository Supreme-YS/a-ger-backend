package com.ireland.ager.account.repository;

import com.ireland.ager.account.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {

<<<<<<< HEAD
    Optional<Account> findUserByAccessToken(String accountId);
    Optional<Account> findUserByAccountEmail(String accountEmail);
=======
    Optional<Account> findAccountByAccessToken(String accountId);

    Boolean existsAccountByAccountEmail(String accountEmail);
    Optional<Account> findAccountByAccountEmail(String accountEmail);

>>>>>>> 8ef6c0112320ce10cf3a37763d18fc944d372d94
}