package com.ireland.ager.account.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.repository.AccountRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthServiceImplTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    Environment env;

    private static String accessToken;

    @Before
    public void setUp() {
        //매번 로그인 수행은 하고 값을 넣어야한다.
        Account newAccount=new Account();
        newAccount.setAccountEmail("dhkstnaos@gmail.com");
        newAccount.setProfileNickname("강완수");
        newAccount.setUserName("강완수");
        newAccount.setProfileImageUrl("http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg");
        newAccount.setAccessToken("3Rr022H5W9LAplRB38hY5P_5-yb_d0uGDMT9IQopyV4AAAF-mZnXTw");
        newAccount.setRefreshToken("TcpwI65wFrM_pLeA_6vd3oc15Dze5hwL8wzYUwopyV4AAAF-mZnXTg");
        accountRepository.save(newAccount);
    }

    @Test
    public void 카카오_로그인_회원가입_성공() throws Exception {
        //given

        //when
        /*ResultActions actions = mockMvc.perform(post("/v1/login")
                .content()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))*/
        //then
    }
}