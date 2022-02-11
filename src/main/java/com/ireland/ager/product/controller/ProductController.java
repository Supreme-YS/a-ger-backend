package com.ireland.ager.product.controller;


import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.SingleResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.main.service.UploadServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.request.ProductUpdateRequest;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
@RequestMapping("/api/product")
public class ProductController {

    private final ProductServiceImpl productService;
    private final AuthServiceImpl authService;
    private final AccountServiceImpl accountService;
    private final ResponseService responseService;
    private final UploadServiceImpl uploadService;


    @GetMapping("/{productId}")
    public ResponseEntity<SingleResult<ProductResponse>> findProductById(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long productId) {
        String[] splitToken = accessToken.split(" ");
        productService.addViewCntToRedis(productId);
        Account accountByAccessToken = accountService.findAccountByAccessToken(splitToken[1]);
        ProductResponse productResponse = ProductResponse.toProductResponse(
                productService.findProductById(productId)
                , accountByAccessToken);
        return new ResponseEntity<>(responseService.getSingleResult(productResponse), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SingleResult<ProductResponse>> createProduct(
            @RequestHeader("Authorization") String accessToken,
            @RequestPart(value = "file") List<MultipartFile> multipartFile,
            @RequestPart(value = "product") @Valid ProductRequest productRequest, BindingResult bindingResult) throws IOException {

        productService.validateUploadForm(bindingResult);
        productService.validateFileExists(multipartFile);
        String[] splitToken = accessToken.split(" ");
        ProductResponse productResponse = productService.createProduct(splitToken[1], productRequest, multipartFile);

        return new ResponseEntity<>(responseService.getSingleResult
                (productResponse), HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<SingleResult<ProductResponse>> updateProduct(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long productId,
            @RequestPart(value = "file") List<MultipartFile> multipartFile,
            @RequestPart(value = "product") @Valid ProductUpdateRequest productUpdateRequest, BindingResult bindingResult) throws IOException {
        productService.validateUploadForm(bindingResult);
        String[] splitToken = accessToken.split(" ");
        ProductResponse productResponse = productService.updateProductById(productId, splitToken[1], multipartFile, productUpdateRequest);
        return new ResponseEntity<>(responseService.getSingleResult(productResponse), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResult> deleteProductById(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable long productId) {
        String[] splitToken = accessToken.split(" ");
        productService.deleteProductById(productId, splitToken[1]);
        return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.OK);
    }
}