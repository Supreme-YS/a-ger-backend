package com.ireland.ager.account.service;

import com.ireland.ager.account.dto.request.AccountUpdatePatchReq;
import com.ireland.ager.account.dto.response.AccountRes;
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


    public AccountRes findAccountByAccountEmail(String accountEmail) {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccountEmail(accountEmail);
        return optionalAccount.map(account -> AccountRes.of(account)).orElse(null);
    }

    public AccountRes findAccountByAccessToken(String accessToken) {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccessToken(accessToken);
        return optionalAccount.map(AccountRes::of).orElse(null);
    }


    public AccountRes insertAccount(Account newAccount) {
        newAccount.setProfileNickname(newAccount.getUserName());
        accountRepository.save(newAccount);
        return AccountRes.of(newAccount);
    }

    public AccountRes updateAccount(AccountUpdatePatchReq accountUpdatePatchReq) {
        Optional<Account> optionalUpdateAccount = accountRepository.findById(accountUpdatePatchReq.getAccountEmail());
        Account updatedAccount = optionalUpdateAccount.map(account -> accountUpdatePatchReq.toAccount(account)).orElse(null);
        if(updatedAccount != null) accountRepository.save(updatedAccount);
        return AccountRes.of(updatedAccount);
    }

    public Boolean deleteAccount(String accountId) {
        accountRepository.deleteById(accountId);
        return Boolean.TRUE;
    }
}
