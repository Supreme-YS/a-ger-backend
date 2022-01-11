package com.ireland.ager.main.controller;

import com.ireland.ager.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class MainController {

    private ProductServiceImpl productService;

    @GetMapping("/")
    public List<Product> listAllProducts() {
        /**
         * @Method : listAllProducts
         * @Description : 등록된 모든 제품의 정보를 불러온다.
         */
        log.info("Select All Products");
        List<Product> productList = productService.getAllProducts();
        return productList;
    }
}
