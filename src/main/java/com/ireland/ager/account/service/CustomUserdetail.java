package com.ireland.ager.account.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.repository.AccountRepository;
import com.ireland.ager.main.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserdetail implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accesstoken) throws UsernameNotFoundException {
        Optional<Account> oUser=accountRepository.findAccountByAccessToken(accesstoken);
        Account user= oUser.orElseThrow(NotFoundException::new);

        return
    }
}
