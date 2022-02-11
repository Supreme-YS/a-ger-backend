package com.ireland.ager.product.controller;


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

    private final ResponseService responseService;
    private final UploadServiceImpl uploadService;


    @GetMapping("/{productId}")
    public ResponseEntity<SingleResult<ProductResponse>> findProductById(
            /**
             * @Method : findProductById
             * @Description : 상품 하나의 정보를 불러온다
             */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long productId) {
        String[] splitToken = accessToken.split(" ");
        productService.addViewCntToRedis(productId);
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
            @RequestPart(value = "product") @Valid ProductRequest productRequest, BindingResult bindingResult) throws IOException {

        //Todo  토큰 까지 확인이 되고 사용자가 입력한 입력 값 검증로직 제목: 공백불가  가격: 공백불가,0이상  내용: 공백 불가
        productService.validateUploadForm(bindingResult);
        //Todo MultipartFile size가 비어있어도 자꾸 1로 뜨는 오류 (1개 선택해서 넣으면 사이즈1, 2개 선택해서 넣으면 2 장난하나?)
        productService.validateFileExists(multipartFile);
        String[] splitToken = accessToken.split(" ");
        ProductResponse productResponse = productService.createProduct(splitToken[1], productRequest, multipartFile);

        return new ResponseEntity<>(responseService.getSingleResult
                (productResponse), HttpStatus.CREATED);
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
            @RequestPart(value = "product") @Valid ProductUpdateRequest productUpdateRequest, BindingResult bindingResult) throws IOException {
        productService.validateUploadForm(bindingResult);
        String[] splitToken = accessToken.split(" ");
        ProductResponse productResponse = productService.updateProductById(productId, splitToken[1], multipartFile, productUpdateRequest);
        return new ResponseEntity<>(responseService.getSingleResult(productResponse), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResult> deleteProductById(
            /**
             * @Method : deleteProductById
             * @Description : 상품 아이디를 기준으로 삭제한다
             */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable long productId) {
        String[] splitToken = accessToken.split(" ");
        productService.deleteProductById(productId, splitToken[1]);
        return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.OK);
    }
}