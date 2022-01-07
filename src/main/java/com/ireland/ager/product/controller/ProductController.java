package com.ireland.ager.product.controller;

import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/{productId}")
    public List<Product> listAllProducts() {
        log.info("Select All Products");
        List<Product> productList = productService.getAllProducts();
        return productList;
    }
}
