package com.ireland.ager.product.controller;


import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.SingleResult;
import com.ireland.ager.main.common.service.ResponseService;
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

    private final ResponseService responseService;


    @GetMapping("/{productId}")
    public ResponseEntity<SingleResult<ProductResponse>> findProductById(
            /**
             * @Method : findProductById
             * @Description : 상품 하나의 정보를 불러온다
             */
            @PathVariable Long productId) {
        return new ResponseEntity<>(responseService.getSingleResult(ProductResponse.toProductResponse
                (productService.findProductById(productId))), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SingleResult<ProductResponse>> createProduct(
            /**
             * @Method : postProduct
             * @Description : 판매자가 판매할 제품을 등록한다.
             */
            @RequestHeader("Authorization") String accessToken,
            @RequestPart(value = "file") List<MultipartFile> multipartFile,
            @RequestPart(value = "product") ProductRequest productRequest) {
            //Todo 토큰값이 유효하지 않을떄
            authService.isValidToken(accessToken);
            //Todo MultipartFile size가 비어있어도 자꾸 1로 뜨는 오류 (1개 선택해서 넣으면 사이즈1, 2개 선택해서 넣으면 2 장난하나?)
            productService.validateFileExists(multipartFile);
            String[] splitToken = accessToken.split(" ");
            return new ResponseEntity<>(responseService.getSingleResult
                    (ProductResponse.toProductResponse(productService.createProduct(splitToken[1], productRequest, multipartFile))), HttpStatus.CREATED);
    }
    @PatchMapping("/{productId}")
    public ResponseEntity<SingleResult<ProductResponse>> updateProduct(
            /**
             * @Method : updateProduct
             * @Description : 상품에 대한 정보를 수정한다.
             */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long productId,
            @RequestPart(value = "file") List<MultipartFile> multipartFile,
            @RequestPart(value = "product") ProductUpdateRequest productUpdateRequest) {

            authService.isValidToken(accessToken);
            String[] splitToken = accessToken.split(" ");
            return new ResponseEntity<>(responseService.getSingleResult(ProductResponse.toProductResponse
                    (productService.updateProductById(productId,splitToken[1],multipartFile,productUpdateRequest))),HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResult> deleteProductById(
            /**
             * @Method : deleteProductById
             * @Description : 상품 아이디를 기준으로 삭제한다
             */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable long productId) {
            //토큰 유효성 검사
            authService.isValidToken(accessToken);
            String[] splitToken = accessToken.split(" ");
            productService.deleteProductById(productId,splitToken[1]);
            return new ResponseEntity<>(responseService.getSuccessResult(),HttpStatus.OK);
    }

    @PatchMapping("/status/{productId}")
    public ResponseEntity<Boolean> setStatus(
            /**
             * @Method : setStatus
             * @Description : 상품 아이디를 기준으로 상품 상태를 변경한다.
             */
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