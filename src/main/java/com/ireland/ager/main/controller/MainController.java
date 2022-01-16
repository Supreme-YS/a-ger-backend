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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
public class MainController {

    private final ProductServiceImpl productService;
    private final ResponseService responseService;

    @GetMapping("/api/product")
    public ResponseEntity<ListResult<ProductResponse>> getlistAllProducts(
            @RequestPart(value = "productId") Long productId,
            @RequestPart(value = "size") Integer size) {
        /**
         * @Method : getlistAllProducts
         * @Description :  페이지{page} 정보와 화면에 보이고 싶은 갯수{size}를 넘겨주면 그에 맞는 페이지를 불러온다.
         */
        return new ResponseEntity<>(responseService.getListResult(
                productService.findProductAllByCreatedAtDesc(productId,size)), HttpStatus.OK);
    }
    @GetMapping("/api/product/views")
    public ResponseEntity<ListResult<ProductResponse>> getlistAllProductsByViewCnt(
            @RequestPart(value = "page") Integer page,
            @RequestPart(value = "size") Integer size) {
        return new ResponseEntity<>(responseService.getListResult(
                productService.findProductAllByProductViewCntDesc(page,size)), HttpStatus.OK);
    }
    @GetMapping("/api/product/category")
    public ResponseEntity<ListResult<ProductResponse>> getlistAllProductsByCategory(
            @RequestPart(value = "page") Integer page,
            @RequestPart(value = "size") Integer size,
            @RequestPart(value = "category") String category) {
        return new ResponseEntity<>(responseService.getListResult(
                productService.findProductAllByCategory(page,size,category)), HttpStatus.OK);
    }
}
