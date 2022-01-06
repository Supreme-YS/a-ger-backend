package com.ireland.ager.account.repository;

import com.ireland.ager.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Account,String> {
    Optional<Account> findUserByAccessToken(String accountId);
}
