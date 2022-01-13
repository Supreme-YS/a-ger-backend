package com.ireland.ager.account.service;

import com.ireland.ager.account.dto.request.AccountUpdateRequest;
import com.ireland.ager.account.dto.response.AccountResponse;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    //TODO redis cache 적용
    @Transactional(readOnly = true)
    public Account findAccountWithProductById(Long accountId) {
        Optional<Account> optionalAccount = accountRepository.findAccountWithProductByAccountId(accountId);
        return optionalAccount.orElse(null);
    }

    public AccountResponse insertAccount(Account newAccount) {
        accountRepository.save(newAccount);
        return AccountResponse.toAccountResponse(newAccount);
    }

    public AccountResponse updateAccount(String accessToken, Long accountId,
                                         AccountUpdateRequest accountUpdateRequest) {
        Account optionalUpdateAccount = findAccountByAccessToken(accessToken);
        if (!(optionalUpdateAccount.getAccountId().equals(accountId))) {
            // 삭제하고자 하는 사람과 현재 토큰 주인이 다르면 False
            return null;
        }
        Account updatedAccount = accountUpdateRequest.toAccount(optionalUpdateAccount);
        if (updatedAccount != null) accountRepository.save(updatedAccount);
        return AccountResponse.toAccountResponse(updatedAccount);
    }

    public Boolean deleteAccount(String accessToken, Long accountId) {
        Account accountByAccessToken = findAccountByAccessToken(accessToken);
        if (!(accountByAccessToken.getAccountId().equals(accountId))) {
            // 삭제하고자 하는 사람과 현재 토큰 주인이 다르면 False
            return Boolean.FALSE;
        }
        accountRepository.deleteById(accountId);
        return Boolean.TRUE;
    }

}

