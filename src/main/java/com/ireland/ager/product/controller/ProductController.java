package com.ireland.ager.product.controller;


import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.request.ProductUpdateRequest;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import com.ireland.ager.product.service.TradeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
@RequestMapping("/api/product")
public class ProductController {

    private final ProductServiceImpl productService;
    private final AuthServiceImpl authService;
    private final TradeServiceImpl tradeService;


    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(
            /**
             * @Method : findProductById
             * @Description : 상품 하나의 정보를 불러온다
             */
            @PathVariable Long productId) {
        log.info("{}", productId);
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(ProductResponse.toProductResponse(product), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            /**
             * @Method : postProduct
             * @Description : 판매자가 판매할 제품을 등록한다.
             */
            @RequestHeader("Authorization") String accessToken,
            @RequestPart(value = "file") List<MultipartFile> multipartFile,
            @RequestPart(value = "product") ProductRequest productRequest) {

        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if (vaildTokenStatusValue == 200) {
            String[] splitToken = accessToken.split(" ");
            Product product = productService.createProduct(splitToken[1], productRequest, multipartFile);
            return new ResponseEntity<>(ProductResponse.toProductResponse(product), HttpStatus.CREATED);
        } else if (vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Boolean> updateProduct(
            /**
             * @Method : updateProduct
             * @Description : 상품에 대한 정보를 수정한다.
             */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long productId,
            @RequestPart(value = "file") List<MultipartFile> multipartFile,
            @RequestPart(value = "product") ProductUpdateRequest productUpdateRequest) {

        int vaildTokenStatusValue = authService.isValidToken(accessToken);
        if (vaildTokenStatusValue == 200) {
            String[] splitToken = accessToken.split(" ");
            Boolean isUpdated = productService.updateProductById(productId, splitToken[1], multipartFile, productUpdateRequest);
            return new ResponseEntity<>(isUpdated, HttpStatus.CREATED);
        } else if (vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProductById(
            /**
             * @Method : deleteProductById
             * @Description : 상품 아이디를 기준으로 삭제한다
             */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable long productId) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if (vaildTokenStatusValue == 200) {
            productService.deleteProductById(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/status/{productId}")
    public ResponseEntity<Boolean> setStatus(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable long productId,
            @RequestPart(value = "status") String status) {
        // 유효한 토큰인지 확인한다.
        int vaildTokenStatusValue = authService.isValidToken(accessToken);
        // 유효한 토큰이라면, 서비스에서 로직을 처리한다.
        if (vaildTokenStatusValue == 200) {
            String[] splitToken = accessToken.split(" ");
            Boolean isUpdated = tradeService.isUpdated(productId, splitToken[1], status);
            return new ResponseEntity<>(isUpdated, HttpStatus.CREATED);
        } else if (vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}