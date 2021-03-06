package com.ireland.ager.account.controller;


import com.ireland.ager.account.service.AccountInfoServiceImpl;
import com.ireland.ager.board.dto.response.BoardSummaryResponse;
import com.ireland.ager.main.common.SliceResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.review.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/account/{accountId}")
@CrossOrigin(value = {"*"}, maxAge = 6000)
public class InfoController {
    private final ResponseService responseService;
    private final AccountInfoServiceImpl accountInfoService;

    @GetMapping("/sells")
    public ResponseEntity<SliceResult<ProductThumbResponse>> findSellsByAccountId(
            @RequestHeader("Authorization") String accessToken
            , @PathVariable Long accountId
            , Pageable pageable) {
        String[] splitToken = accessToken.split(" ");
        return new ResponseEntity<>(responseService.getSliceResult(accountInfoService.findSellsByAccountId(accountId, pageable)), HttpStatus.CREATED);
    }

    @GetMapping("/buys")
    public ResponseEntity<SliceResult<ProductThumbResponse>> findBuysByAccountId(
            @RequestHeader("Authorization") String accessToken
            , @PathVariable Long accountId
            , Pageable pageable) {
        String[] splitToken = accessToken.split(" ");
        return new ResponseEntity<>(responseService.getSliceResult(accountInfoService.findBuysByACcountId(splitToken[1],accountId, pageable)), HttpStatus.CREATED);
    }

    @GetMapping("/reviews")
    public ResponseEntity<SliceResult<ReviewResponse>> findReviewsByAccountId(
            @RequestHeader("Authorization") String accessToken
            , @PathVariable Long accountId
            , Pageable pageable) {
        String[] splitToken = accessToken.split(" ");
        return new ResponseEntity<>(responseService.getSliceResult(accountInfoService.findReviewsByAccountId(accountId, pageable)), HttpStatus.CREATED);
    }

    @GetMapping("/boards")
    public ResponseEntity<SliceResult<BoardSummaryResponse>> findBoardsByAccountId(
            @RequestHeader("Authorization") String accessToken
            , @PathVariable Long accountId
            , Pageable pageable) {
        String[] splitToken = accessToken.split(" ");
        return new ResponseEntity<>(responseService.getSliceResult(accountInfoService.findBoardsByAccountId(accountId, pageable)), HttpStatus.CREATED);
    }
}