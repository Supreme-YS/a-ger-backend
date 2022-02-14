package com.ireland.ager.account.repository;

import com.ireland.ager.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

    Optional<Account> findAccountByAccessToken(String accessToken);
    Optional<Account> findAccountByAccountEmail(String accountEmail);


}