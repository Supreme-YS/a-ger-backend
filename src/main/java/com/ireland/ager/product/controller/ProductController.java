package com.ireland.ager.product.controller;

import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "/")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 헤더 정보에 액세스 토큰이 발급되었다는 가정하에
    @GetMapping(value = "product")
    public List<Product> listAllProducts(Long productId) {
        log.info("Select All Products");
        List<Product> productList = productService.getAllProducts(productId);
        return productList;
    }
}
