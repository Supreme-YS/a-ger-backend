package com.ireland.ager.trade.service;

import com.ireland.ager.trade.entity.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MannerServiceImpl {

    public void postReview(Trade trade, String accessToken) {
        // 거래 정보를 불러옴

        // 거래 정보에 따른 계정 정보들을 불러옴 (판매자, 구매자)

        // 구매자는 판매자에 대한 정보를 남긴다.
    }
}
