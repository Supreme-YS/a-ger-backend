package com.ireland.ager.trade.controller;

import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.trade.service.TradeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
@RequestMapping("/api/trade")
public class TradeController {
    private final TradeServiceImpl tradeService;
    private final AuthServiceImpl authService;

    private final ResponseService responseService;
    @PostMapping("/{roomId}")
    public ResponseEntity<CommonResult> setStatus(
            /**
             * @Method : setStatus
             * @Description : 상품 아이디를 기준으로 상품 상태를 변경한다.
             */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long roomId,
            @RequestParam String status) {
        String[] splitToken = accessToken.split(" ");
        tradeService.isUpdated(roomId, splitToken[1], status);
        return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }
}
