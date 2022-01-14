package com.ireland.ager.trade.controller;

import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TradeController {

    private final ProductServiceImpl productService;

    @GetMapping("{productId}")
    // TradeResponse 응답 객체의 내용
    public ResponseEntity<TradeResponse> startTradeByTradeId(
            @PathVariable Long productId) {
        // 제품 정보를 가져옴
        Product product = productService.findProductById(productId);
        // 제품 정보의 거래 상태를 변경한다

    }

}
