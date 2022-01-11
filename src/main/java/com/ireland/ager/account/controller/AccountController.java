package com.ireland.ager.account.controller;


import com.ireland.ager.account.dto.request.AccountUpdatePatchReq;
import com.ireland.ager.account.dto.response.AccountRes;
import com.ireland.ager.account.dto.response.KakaoAccountRes;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin( value = {"*"}, maxAge = 6000)
public class AccountController {
    private  final AccountServiceImpl accountService;

    private  final AuthServiceImpl authService;

    private final String EMAIL_SUFFIX = "@gmail.com";

    @GetMapping("/kakao/showlogin")
    public ResponseEntity<String> showLogin() {
        return new ResponseEntity<>(authService.getKakaoLoginUrl(), HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<AccountRes> getTokenAndJoinOrLogin(@RequestParam("code") String code) {
        HashMap<String, String> kakaoTokens = authService.getKakaoTokens(code);

        KakaoAccountRes kakaoAccountRes = authService.getKakaoUserInfo(kakaoTokens.get("access_token"));

        String accountEmailOrId = kakaoAccountRes.getKakao_account().getEmail();
        if(accountEmailOrId == null || accountEmailOrId == "") {
            accountEmailOrId = String.valueOf(kakaoAccountRes.getId()) + EMAIL_SUFFIX;
        }
        Account accountForCheck = accountService.findAccountByAccountEmail(accountEmailOrId);
        AccountRes accountRes;
        if(accountForCheck != null) {
            // 존재한다면 Token 값을 갱신하고 반환한다.
            accountRes = authService.refreshTokensForExistAccount(accountForCheck.getAccountId(), kakaoTokens.get("access_token"), kakaoTokens.get("refresh_token"));
        } else {
            // 존재하지 않는다면 회원 가입 시키고 반환한다.
            accountRes = accountService.insertAccount(
                    kakaoAccountRes.toUser(kakaoTokens.get("access_token"), kakaoTokens.get("refresh_token")));
        }

        return new ResponseEntity<>(accountRes, HttpStatus.OK);
    }

    @GetMapping("/kakao/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken) {

        int vaildTokenStatusValue = authService.isValidToken(accessToken);
        log.info("accessToken : {}",accessToken);
        log.info("vaildTokenStatusValue : {}",vaildTokenStatusValue);
        if(vaildTokenStatusValue == 200) {
<<<<<<< HEAD
            String[] spitToken = accessToken.split(" ");
            Account account = accountService.findAccountByAccessToken(spitToken[1]);
=======
            String[] splitToken = accessToken.split(" ");
            Account account = accountService.findAccountByAccessToken(splitToken[1]);
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
            authService.logout(account);
            log.info("userRes : {}", account);
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/kakao/update/{accountId}")
    public ResponseEntity<String> accessTokenUpdate(@PathVariable Long accountId) {
        String newToken = authService.accessTokenUpdate(accountId);
        if(newToken == null) {
            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>(newToken, HttpStatus.OK);
    }


    @PatchMapping("/user")
    public ResponseEntity<AccountRes> updateUser(
            @RequestHeader("Authorization") String accessToken, @RequestBody AccountUpdatePatchReq accountUpdatePatchReq) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
<<<<<<< HEAD
            String[] spitToken = accessToken.split(" ");
            AccountRes accountRes = accountService.updateAccount(spitToken[1],accountUpdatePatchReq);
=======
            String[] splitToken = accessToken.split(" ");
            AccountRes accountRes = accountService.updateAccount(splitToken[1],accountUpdatePatchReq);
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
            return new ResponseEntity<>(accountRes, HttpStatus.OK);
        }
        else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/user")
    public ResponseEntity<Account> getMyInfo( @RequestHeader("Authorization") String accessToken) {
        int validTokenStatusValue = authService.isValidToken(accessToken);

        if(validTokenStatusValue == 200) {
<<<<<<< HEAD
            String[] spitToken = accessToken.split(" ");
            Account account = accountService.findAccountByAccessToken(spitToken[1]);
=======
            String[] splitToken = accessToken.split(" ");
            Account account = accountService.findAccountByAccessToken(splitToken[1]);
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{accountId}")
    public ResponseEntity<AccountRes> getOtherUserInfo( @RequestHeader("Authorization") String accessToken,
<<<<<<< HEAD
                                                    @PathVariable Long accountId) {
        int validTokenStatusValue = authService.isValidToken(accessToken);

        if(validTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            Account account = accountService.findAccountById(accountId);
            return new ResponseEntity<>(AccountRes.of(account), HttpStatus.OK);
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/user/{accountId}")
    public ResponseEntity<Boolean> deleteAccount( @RequestHeader("Authorization") String accessToken,
        @PathVariable Long accountId) {
        int validTokenStatusValue = authService.isValidToken(accessToken);
        //유효성 체크만 한다.
        if(validTokenStatusValue == 200) {
            Boolean isDeleted = accountService.deleteAccount(accountId);
            return new ResponseEntity<>(isDeleted,HttpStatus.OK);
=======
                                                        @PathVariable Long accountId) {
        int validTokenStatusValue = authService.isValidToken(accessToken);

        if(validTokenStatusValue == 200) {
            String[] splitToken = accessToken.split(" ");
            Account account = accountService.findAccountById(accountId);
            return new ResponseEntity<>(AccountRes.of(account), HttpStatus.OK);
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
<<<<<<< HEAD
}
=======

    @DeleteMapping("/user/{accountId}")
    public ResponseEntity<Boolean> deleteAccount( @RequestHeader("Authorization") String accessToken,
                                                  @PathVariable Long accountId) {
        int validTokenStatusValue = authService.isValidToken(accessToken);
        //유효성 체크만 한다.
        if(validTokenStatusValue == 200) {
            Boolean isDeleted = accountService.deleteAccount(accountId);
            return new ResponseEntity<>(isDeleted,HttpStatus.OK);
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
