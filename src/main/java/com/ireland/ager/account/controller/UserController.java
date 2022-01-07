package com.ireland.ager.account.controller;


import com.ireland.ager.account.dto.response.KakaoUserRes;
import com.ireland.ager.account.dto.response.UserRes;
import com.ireland.ager.account.dto.request.UserUpdatePatchReq;
import com.ireland.ager.account.service.AuthService;
import com.ireland.ager.account.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin( value = {"*"}, maxAge = 6000)
public class UserController {
    private  final UserService userService;

    private  final AuthService authService;

    private final String EMAIL_SUFFIX = "@gmail.com";

    @GetMapping("/kakao/showlogin")
    public ResponseEntity<String> showLogin() {
        return new ResponseEntity<>(authService.getKakaoLoginUrl(), HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<UserRes> getTokenAndJoinOrLogin(@RequestParam("code") String code) {
        log.info("Code : {}",code);
        // 1. Token을 발급받는다.
        HashMap<String, String> kakaoTokens = authService.getKakaoTokens(code);

        // 2. Token 값을 통해 UserInfo를 받아온다.
        KakaoUserRes kakaoUserRes = authService.getKakaoUserInfo(kakaoTokens.get("access_token"));

        // 3. UserInfo의 내용이 회원 DB에 존재하는가?
        String accountEmailOrId = kakaoUserRes.getKakao_account().getEmail();
        if(accountEmailOrId == null || accountEmailOrId == "") {
            accountEmailOrId = String.valueOf(kakaoUserRes.getId()) + EMAIL_SUFFIX;
        }
        UserRes userResForCheck = userService.findUserByAccountEmail(accountEmailOrId);

        UserRes userRes;
        if(userResForCheck != null) {
            // 존재한다면 Token 값을 갱신하고 반환한다.
            userRes = authService.refreshTokensForExistUser(userResForCheck.getAccountEmail(), kakaoTokens.get("access_token"), kakaoTokens.get("refresh_token"));
        } else {
            // 존재하지 않는다면 회원 가입 시키고 반환한다.
            userRes = userService.insertUser(kakaoUserRes.toUser(kakaoTokens.get("access_token"), kakaoTokens.get("refresh_token")));
        }

        return new ResponseEntity<>(userRes, HttpStatus.OK);
    }

    @GetMapping("/kakao/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken) {

        int vaildTokenStatusValue = authService.isValidToken(accessToken);
        log.info("accessToken : {}",accessToken);
        log.info("vaildTokenStatusValue : {}",vaildTokenStatusValue);
        if(vaildTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            UserRes userRes = userService.findUserByAccessToken(spitToken[1]);
            authService.logout(userRes);
            log.info("userRes : {}",userRes);
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/kakao/update/{accountEmail}")
    public ResponseEntity<String> accessTokenUpdate(@PathVariable("accountEmail") String accountEmail) {
        String newToken = authService.accessTokenUpdate(accountEmail);
        if(newToken == null) {
            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>(newToken, HttpStatus.OK);
    }
    @PatchMapping("/user")
    public ResponseEntity<UserRes> updateUser(
            @RequestHeader("Authorization") String accessToken, @RequestBody UserUpdatePatchReq userUpdatePatchReq) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
            UserRes userRes = userService.updateUser(userUpdatePatchReq);
            return new ResponseEntity<>(userRes, HttpStatus.OK);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user")
    public ResponseEntity<UserRes> getMyInfo( @RequestHeader("Authorization") String accessToken) {
        int validTokenStatusValue = authService.isValidToken(accessToken);

        if(validTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            UserRes userRes = userService.findUserByAccessToken(spitToken[1]);
            return new ResponseEntity<>(userRes, HttpStatus.OK);
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{accountEmail}")
    public ResponseEntity<UserRes> getOtherUserInfo( @RequestHeader("Authorization") String accessToken,
                                                    @PathVariable("accountEmail") String accountEmail) {
        int validTokenStatusValue = authService.isValidToken(accessToken);

        if(validTokenStatusValue == 200) {
            UserRes userRes = userService.findUserByAccountEmail(accountEmail);
            return new ResponseEntity<>(userRes, HttpStatus.OK);
        } else if(validTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
