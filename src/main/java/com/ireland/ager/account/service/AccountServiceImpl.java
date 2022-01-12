package com.ireland.ager.account.service;

import com.ireland.ager.account.dto.request.AccountUpdateRequest;
import com.ireland.ager.account.dto.response.AccountResponse;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.repository.AccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl {
    private final AccountRepository accountRepository;


    public Account findAccountByAccountEmail(String accountEmail) {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccountEmail(accountEmail);
        return optionalAccount.orElse(null);
    }

    public Account findAccountById(Long accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        return optionalAccount.orElse(null);
    }


    public Account findAccountByAccessToken(String accessToken) {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccessToken(accessToken);
        return optionalAccount.orElse(null);
    }

    public AccountResponse insertAccount(Account newAccount) {
        newAccount.setProfileNickname(newAccount.getUserName());
        accountRepository.save(newAccount);
        return AccountResponse.of(newAccount);
    }
    public AccountResponse updateAccount(String accessToken, AccountUpdateRequest accountUpdateRequest) {
        Optional<Account> optionalUpdateAccount = accountRepository.findAccountByAccessToken(accessToken);
        Account updatedAccount = optionalUpdateAccount.map(accountUpdateRequest::toAccount).orElse(null);
        if (updatedAccount != null) accountRepository.save(updatedAccount);
        return AccountResponse.of(updatedAccount);
    }

    public Boolean deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
        return Boolean.TRUE;
    }

    public Account findAccountWithProductById(Long accountId) {
        Optional<Account> optionalAccount = accountRepository.findWithProductByAccountId(accountId);
        return optionalAccount.orElse(null);
    }

}

