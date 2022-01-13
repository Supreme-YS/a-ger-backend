package com.ireland.ager.account.controller;


import com.ireland.ager.account.dto.request.AccountUpdateRequest;
import com.ireland.ager.account.dto.response.AccountResponse;
import com.ireland.ager.account.dto.response.KakaoResponse;
import com.ireland.ager.account.dto.response.OtherAccountResponse;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/account")
@CrossOrigin( value = {"*"}, maxAge = 6000)
public class AccountController {
    private  final AccountServiceImpl accountService;

    private  final AuthServiceImpl authService;

    private final String EMAIL_SUFFIX = "@gmail.com";
    @GetMapping("/login-url")
    public ResponseEntity<String> loginUrl() {
        return new ResponseEntity<>(authService.getKakaoLoginUrl(), HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<AccountResponse> getTokenAndJoinOrLogin(@RequestParam("code") String code) {
        HashMap<String, String> kakaoTokens = authService.getKakaoTokens(code);

        KakaoResponse kakaoResponse = authService.getKakaoUserInfo(kakaoTokens.get("access_token"));

        String accountEmailOrId = kakaoResponse.getKakao_account().getEmail();
        if(accountEmailOrId == null || accountEmailOrId == "") {
            accountEmailOrId = String.valueOf(kakaoResponse.getId()) + EMAIL_SUFFIX;
        }
        Account accountForCheck = accountService.findAccountByAccountEmail(accountEmailOrId);
        AccountResponse accountResponse;
        if(accountForCheck != null) {
            // 존재한다면 Token 값을 갱신하고 반환한다.
            accountResponse = authService.updateTokenWithAccount(accountForCheck.getAccountId(), kakaoTokens.get("access_token"), kakaoTokens.get("refresh_token"));
        } else {
            // 존재하지 않는다면 회원 가입 시키고 반환한다.
            accountResponse = accountService.insertAccount(
                kakaoResponse.toAccount(kakaoTokens.get("access_token"), kakaoTokens.get("refresh_token")));
        }

        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestHeader("Authorization") String accessToken) {

        int vaildTokenStatusValue = authService.isValidToken(accessToken);
        log.info("accessToken : {}",accessToken);
        if(vaildTokenStatusValue == 200) {
            Account accountByAccessToken = accountService.findAccountByAccessToken(accessToken.split(" ")[1]);
            authService.logout(accountByAccessToken);
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(Boolean.FALSE,HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(Boolean.FALSE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/token/{accountId}")
    public ResponseEntity<String> updateAccessToken(@PathVariable Long accountId) {
        String newToken = authService.updateAccessToken(accountId);
        if(newToken == null) {
            return new ResponseEntity<>("FALSE", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>(newToken, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AccountResponse> getMyAccount(@RequestHeader("Authorization") String accessToken) {
        int validTokenStatusValue = authService.isValidToken(accessToken);

        if(validTokenStatusValue == 200) {
            String[] splitToken = accessToken.split(" ");
            Account account = accountService.findAccountByAccessToken(splitToken[1]);
            return new ResponseEntity<>(AccountResponse.toAccountResponse(account), HttpStatus.OK);
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<OtherAccountResponse> getOtherAccount(
        @RequestHeader("Authorization") String accessToken,
        @PathVariable Long accountId)
    {
        int validTokenStatusValue = authService.isValidToken(accessToken);
        if(validTokenStatusValue == 200) {
            Account account = accountService.findAccountWithProductById(accountId);
            return new ResponseEntity<>(OtherAccountResponse.toOtherAccountResponse(account), HttpStatus.OK);
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
        @RequestHeader("Authorization") String accessToken,
        @PathVariable Long accountId,
        @RequestBody AccountUpdateRequest accountUpdateRequest) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {

            String[] spitToken = accessToken.split(" ");
            AccountResponse accountResponse = accountService.updateAccount(spitToken[1],accountId,accountUpdateRequest);

            return new ResponseEntity<>(accountResponse, HttpStatus.OK);
        }
        else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Boolean> deleteAccount(
        @RequestHeader("Authorization") String accessToken,
        @PathVariable Long accountId)
    {
        int validTokenStatusValue = authService.isValidToken(accessToken);
        //TODO accessToken으로 조회한 유저와 같을때만 삭제할 수 있도록 해야한다.
        if(validTokenStatusValue == 200) {
            String[] splitToken = accessToken.split(" ");
            Boolean isDeleted = accountService.deleteAccount(splitToken[1],accountId);
            return new ResponseEntity<>(isDeleted,HttpStatus.OK);
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}