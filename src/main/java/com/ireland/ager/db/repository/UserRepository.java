package com.ireland.ager.db.repository;

import com.ireland.ager.db.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Account,String> {
    Optional<Account> findUserByAccessToken(String  accountId);
}
