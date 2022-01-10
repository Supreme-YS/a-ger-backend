package com.ireland.ager.product.controller;

import com.ireland.ager.account.dto.response.AccountRes;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.request.ProductUpdateRequest;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.repository.ProductRepository;
import com.ireland.ager.product.service.ProductServiceImpl;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private final ProductRepository productRepository;

    @PostMapping(value = "/post")
    public ResponseEntity<Product> postProduct(
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
            Product product = productService.postProduct(splitToken[1], productRequest, multipartFile);
            return new ResponseEntity<>(product, HttpStatus.CREATED);

        } else if (vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findProductById(
            /**
             * @Method : findProductById
             * @Description : 상품 하나의 정보를 불러온다
             */
            @PathVariable Long productId) {
        log.info("{}", productId);
        Optional<Product> product = productService.findProductById(productId);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
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
            // account 관련한 처리가 더 필요하지 않나?.. by Jacob
            // 토큰 정보가 담긴 account 정보가 넘어옴
            Account account = accountService.findAccountByAccessToken(splitToken[1]);
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
}
