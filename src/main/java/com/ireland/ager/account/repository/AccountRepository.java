package com.ireland.ager.account.repository;

import com.ireland.ager.account.entity.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByAccessToken(String accessToken);

    Boolean existsAccountByAccountEmail(String accountEmail);

    Optional<Account> findAccountByAccountEmail(String accountEmail);

    @EntityGraph("Account.withProductAndUrl")
    Optional<Account> findAccountWithProductByAccountId(Long accountId);

}