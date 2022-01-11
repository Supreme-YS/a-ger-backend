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


    public Account findAccountByAccountEmail(String accountEmail) {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccountEmail(accountEmail);
        return optionalAccount.orElse(null);
    }

    public Account findAccountById(Long accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        return optionalAccount.orElse(null);
<<<<<<< HEAD
    }

    public Account findAccountByAccessToken(String accessToken) {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccessToken(accessToken);
        return optionalAccount.orElse(null);
=======
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
    }

    public Account findAccountByAccessToken(String accessToken) {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccessToken(accessToken);
        return optionalAccount.orElse(null);
    }

    public AccountRes insertAccount(Account newAccount) {
        newAccount.setProfileNickname(newAccount.getUserName());
        accountRepository.save(newAccount);
        return AccountRes.of(newAccount);
    }

    public AccountRes updateAccount(String accessToken, AccountUpdatePatchReq accountUpdatePatchReq) {
        Optional<Account> optionalUpdateAccount = accountRepository.findAccountByAccessToken(accessToken);
        Account updatedAccount = optionalUpdateAccount.map(accountUpdatePatchReq::toAccount).orElse(null);
<<<<<<< HEAD
        if(updatedAccount != null) accountRepository.save(updatedAccount);
=======
        if (updatedAccount != null) accountRepository.save(updatedAccount);
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
        return AccountRes.of(updatedAccount);
    }

    public Boolean deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
        return Boolean.TRUE;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
