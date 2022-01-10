package com.ireland.ager.account.service;

import com.ireland.ager.account.dto.response.AccountRes;
import com.ireland.ager.account.dto.response.KakaoAccountRes;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.repository.AccountRepository;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final AccountRepository accountRepository;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoRestApiKey;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoRestSecretKey;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUrl;

    private final String KAKAO_URL = "https://kauth.kakao.com";
    private final String TOKEN_TYPE = "Bearer";

    public String getKakaoLoginUrl() {
        return new StringBuilder()
                .append(KAKAO_URL).append("/oauth/authorize?client_id=").append(kakaoRestApiKey)
                .append("&redirect_uri=").append(kakaoRedirectUrl)
                .append("&response_type=code")
                .toString();
    }

    public HashMap<String, String> getKakaoTokens(String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("grant_type", "authorization_code");
        httpBody.add("client_id", kakaoRestApiKey);
        httpBody.add("redirect_uri", kakaoRedirectUrl);
        httpBody.add("code", code);
        httpBody.add("client_secret", kakaoRestSecretKey);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenReq = new HttpEntity<>(httpBody, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap> tokenResEntity = restTemplate.exchange(KAKAO_URL + "/oauth/token", HttpMethod.POST, kakaoTokenReq, HashMap.class);

        return tokenResEntity.getBody();
    }

    public KakaoAccountRes getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders.add("Authorization", TOKEN_TYPE + " " + accessToken);

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoReq = new HttpEntity<>(httpHeaders);
        ResponseEntity<KakaoAccountRes> userInfo = restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.GET, kakaoUserInfoReq, KakaoAccountRes.class);

        return userInfo.getBody();
    }

    public AccountRes refreshTokensForExistAccount(Long accountId, String accessToken, String refreshToken) {
        Optional<Account> optionalExistAccount = accountRepository.findById(accountId);
        Account existAccount = optionalExistAccount.map(account -> {
            account.setAccessToken(accessToken);
            account.setRefreshToken(refreshToken);
            return account;
        }).orElse(null);

        accountRepository.save(existAccount);
        return AccountRes.of(existAccount);
    }

    public String accessTokenUpdate(Long accountId) {
        Optional<Account> optionalAccountForUpdate = accountRepository.findById(accountId);
        String refreshToken = optionalAccountForUpdate.map(Account::getRefreshToken).orElse(null);
        if(refreshToken == null) return null;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("grant_type", "refresh_token");
        httpBody.add("client_id", kakaoRestApiKey);
        httpBody.add("refresh_token", refreshToken);
        httpBody.add("client_secret", kakaoRestSecretKey);

        HttpEntity<MultiValueMap<String, String>> kakaoUpdateTokenReq = new HttpEntity<>(httpBody, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap> tokenResEntity = restTemplate.exchange(KAKAO_URL + "/oauth/token", HttpMethod.POST, kakaoUpdateTokenReq, HashMap.class);

        String newToken = (String) tokenResEntity.getBody().get("access_token");
        optionalAccountForUpdate.get().setAccessToken(newToken);
        accountRepository.save(optionalAccountForUpdate.get());

        return newToken;
    }

    public int isValidToken(String accessToken) {
        String vaildCheckHost = "https://kapi.kakao.com/v1/user/access_token_info";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders.add("Authorization",accessToken);
        HttpEntity<MultiValueMap<String, String>> kakaoValidTokenReq = new HttpEntity<>(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<HashMap> isValidEntity = restTemplate.exchange(vaildCheckHost, HttpMethod.GET, kakaoValidTokenReq, HashMap.class);
            return isValidEntity.getStatusCodeValue();
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode().value() == 401) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED).getStatusCodeValue();
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCodeValue();
            }
        }
    }

    public void logout(Account account) {
        String logoutHost = "https://kapi.kakao.com/v1/user/logout";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders.add("Authorization", TOKEN_TYPE + " " + account.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> logoutKakaoReq = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(logoutHost, HttpMethod.POST, logoutKakaoReq, HashMap.class);

        Optional<Account> optionalLogoutAccount = accountRepository.findById(account.getAccountId());
        optionalLogoutAccount.ifPresent(accountRepository::save);
    }
}
