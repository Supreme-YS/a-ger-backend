package com.ireland.ager.product.controller;

import com.ireland.ager.account.dto.response.AccountRes;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
@RequestMapping("/product")
public class ProductController {
    private final ProductServiceImpl productService;
    private final AuthServiceImpl authService;
    private final AccountServiceImpl accountService;
    @PostMapping
    public ResponseEntity<Product> postProduct(
        @RequestHeader("Authorization") String accessToken,
        @RequestPart(value = "file") List<MultipartFile> multipartFile,
        @RequestPart(value="product") ProductRequest productRequest) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            Product product = productService.postProduct(spitToken[1],productRequest,
                multipartFile);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
        @Method: updateProduct
        @Author: frank
        @content: 상품 수정
        @return: Product
     */
    @PatchMapping({"productId"})
    public ResponseEntity<Product> updateProduct(
        @RequestHeader("Authorization") String accessToken,
        @PathVariable Long productId,
        @RequestPart(value = "file") List<MultipartFile> multipartFile,
        @RequestPart(value="product") ProductRequest productRequest) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            AccountRes userRes = accountService.findAccountByAccessToken(spitToken[1]);
            //TODO: 220110 Account와 Product 관계 설정
            //TODO: 220110 1. accessToken 확인
            //TODO: 220110 2. accessToken으로 accountId 조회한다.
            //TODO: 220110 

            Product product = productService.postProduct(accessToken, productRequest,
                multipartFile);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public List<Product> listAllProducts() {
        log.info("Select All Products");
        List<Product> productList = productService.getAllProducts();
        return productList;
    }
}
