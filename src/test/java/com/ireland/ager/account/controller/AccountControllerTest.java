package com.ireland.ager.account.controller;

import com.ireland.ager.account.repository.AccountRepository;
import com.ireland.ager.account.service.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountControllerTest {
    @Autowired
    AuthServiceImpl authService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void 로그인_URL_가져오기_성공() throws Exception {
    }
}