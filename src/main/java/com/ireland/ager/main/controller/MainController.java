package com.ireland.ager.main.controller;

import com.ireland.ager.main.common.ListResult;
import com.ireland.ager.main.common.SliceResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @GetMapping("/api/product/search")
    public ResponseEntity<SliceResult<ProductThumbResponse>> searchAllProducts(
            @RequestParam(value = "category",required = false)Category category
            ,@RequestParam(value = "keyword",required = false) String keyword
            ,Pageable pageable) {
        return new ResponseEntity<>(responseService.getSliceResult(
                productService.findProductAllByCreatedAtDesc(category,keyword,pageable)), HttpStatus.OK);
    }
}
