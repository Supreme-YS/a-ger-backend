package com.ireland.ager.main.controller;

import com.ireland.ager.main.common.ListResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
public class MainController {

    private final ProductServiceImpl productService;
    private final ResponseService responseService;

    @GetMapping("/api/product/time")
    public ResponseEntity<ListResult<ProductResponse>> getlistAllProducts(
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "size") Integer size) {
        /**
         * @Method : getlistAllProducts
         * @Description :  프론트에서 리스트 중에 가장 작은 페이지아이디{productId}와 화면에 보일 갯수{size} 를넘겨준다.
         */
        return new ResponseEntity<>(responseService.getListResult(
                productService.findProductAllByCreatedAtDesc(productId,size)), HttpStatus.OK);
    }

    @GetMapping("/api/product/views")
    public ResponseEntity<ListResult<ProductResponse>> getlistAllProductsByViewCnt(
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "size") Integer size) {
        return new ResponseEntity<>(responseService.getListResult(
                productService.findProductAllByProductViewCntDesc(productId,size)), HttpStatus.OK);
    }

    @GetMapping("/api/product/category")
    public ResponseEntity<ListResult<ProductResponse>> getlistAllProductsByCategory(
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "category") String category) {
        return new ResponseEntity<>(responseService.getListResult(
                productService.findProductAllByCategory(productId,size,category)), HttpStatus.OK);
    }
}
