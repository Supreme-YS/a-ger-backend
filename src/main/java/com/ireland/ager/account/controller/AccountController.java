package com.ireland.ager.account.controller;

import com.ireland.ager.account.dto.request.AccountUpdateRequest;
import com.ireland.ager.account.dto.response.MyAccountResponse;
import com.ireland.ager.account.dto.response.OtherAccountResponse;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.SingleResult;
import com.ireland.ager.main.common.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/account")
@CrossOrigin(value = {"*"}, maxAge = 6000)
public class AccountController {
    private final AccountServiceImpl accountService;

    private final AuthServiceImpl authService;

    private final ResponseService responseService;


    @GetMapping("/login-url")
    public ResponseEntity<SingleResult<String>> loginUrl() {
        log.info("test");
        return new ResponseEntity<>(responseService.getSingleResult(authService.getKakaoLoginUrl()), HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<SingleResult<MyAccountResponse>> getTokenAndJoinOrLogin(@RequestParam("code") String code) {
        return new ResponseEntity<>(responseService.getSingleResult(authService.getKakaoLogin(code)), HttpStatus.CREATED);
    }

    @GetMapping("/logout")
    public ResponseEntity<CommonResult> logout(@RequestHeader("Authorization") String accessToken) {
        authService.getKakaoLogout(accessToken);
        return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.OK);
    }

    @GetMapping("/token/{accountId}")
    public ResponseEntity<SingleResult<String>> updateAccessToken(@PathVariable Long accountId) {
        String newToken = authService.updateAccessToken(accountId);
        return new ResponseEntity<>(responseService.getSingleResult(newToken), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SingleResult<MyAccountResponse>> getMyAccount(@RequestHeader("Authorization") String accessToken) {
        String[] splitToken = accessToken.split(" ");
        MyAccountResponse myAccountResponse = MyAccountResponse.toAccountResponse(accountService.findAccountByAccessToken(splitToken[1]));
        return new ResponseEntity<>(
                responseService.getSingleResult(myAccountResponse), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<SingleResult<OtherAccountResponse>> getOtherAccount(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long accountId) {
        String[] splitToken = accessToken.split(" ");
        OtherAccountResponse otherAccountByAccountId =OtherAccountResponse.toOtherAccountResponse(accountService.findAccountByAccessToken(splitToken[1]));
        return new ResponseEntity<>(
                responseService.getSingleResult(otherAccountByAccountId), HttpStatus.OK);
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<SingleResult<MyAccountResponse>> updateAccount(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long accountId,
            @RequestPart(value = "update") AccountUpdateRequest accountUpdateRequest,
            @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
        String[] spitToken = accessToken.split(" ");
        MyAccountResponse myAccountResponse = accountService.updateAccount(spitToken[1], accountId, accountUpdateRequest, multipartFile);
        return new ResponseEntity<>(responseService.getSingleResult(myAccountResponse), HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<CommonResult> deleteAccount(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long accountId) {
        String[] splitToken = accessToken.split(" ");
        accountService.deleteAccount(splitToken[1], accountId);
        return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.OK);
    }
}