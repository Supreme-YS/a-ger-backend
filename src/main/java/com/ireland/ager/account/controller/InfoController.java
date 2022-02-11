package com.ireland.ager.account.controller;

import com.ireland.ager.account.dto.request.AccountUpdateRequest;
import com.ireland.ager.account.dto.response.MyAccountResponse;
import com.ireland.ager.account.dto.response.OtherAccountResponse;
import com.ireland.ager.account.service.AccountInfoServiceImpl;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.ListResult;
import com.ireland.ager.main.common.SingleResult;
import com.ireland.ager.main.common.SliceResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.review.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/account/{accountId}")
@CrossOrigin(value = {"*"}, maxAge = 6000)
public class InfoController {
    private final AccountServiceImpl accountService;
    private final AuthServiceImpl authService;
    private final ResponseService responseService;
    private final AccountInfoServiceImpl accountInfoService;

    @GetMapping("/sells")
    public ResponseEntity<SliceResult<ProductThumbResponse>> findSellsByAccountId(
            @RequestHeader("Authorization") String accessToken
            , @PathVariable Long accountId
            , Pageable pageable
            ) {
        String[] splitToken = accessToken.split(" ");
        return new ResponseEntity<>(responseService.getSliceResult(accountInfoService.findSellsByAccountId(accountId,pageable)) , HttpStatus.CREATED);
    }
}