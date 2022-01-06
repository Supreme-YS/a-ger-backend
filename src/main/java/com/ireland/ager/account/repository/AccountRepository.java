package com.ireland.ager.account.repository;

import com.ireland.ager.account.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
    Optional<Account> findUserByAccessToken(String  accountId);
    Optional<Account> findUserByAccountEmail(String  accountEmail);
}
