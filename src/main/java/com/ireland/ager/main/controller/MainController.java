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

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
public class MainController {

    private final ProductServiceImpl productService;
    private final ResponseService responseService;
    @GetMapping("/api/product/latest")
    public ResponseEntity<ListResult<ProductResponse>> getListAllProductsByCreatedAt() {
        List<ProductResponse> productResponseList= productService.findProductAllOrderByCreatedAtDesc();
        return new ResponseEntity<>(responseService.getListResult(productResponseList), HttpStatus.OK);
    }
    @GetMapping("/api/product/views")
    public ResponseEntity<ListResult<ProductResponse>> getListAllProductsByProductViewCnt() {
        List<ProductResponse> productResponseList= productService.findProductAllOrderByProductViewCntDesc();
        return new ResponseEntity<>(responseService.getListResult(productResponseList), HttpStatus.OK);
    }
    @GetMapping("/api/product")
    public ResponseEntity<ListResult<ProductResponse>> getListAllProductsByCategory(
            @RequestPart(value = "category") String category
    ) {
        List<ProductResponse> productResponseList= productService.findProductAllByCategory(category);
        return new ResponseEntity<>(responseService.getListResult(productResponseList), HttpStatus.OK);
    }
}
