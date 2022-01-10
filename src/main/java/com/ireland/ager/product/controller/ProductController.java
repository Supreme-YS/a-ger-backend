package com.ireland.ager.product.controller;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.request.ProductUpdateRequest;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.repository.ProductRepository;
import com.ireland.ager.product.service.ProductServiceImpl;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
@RequestMapping("product")
public class ProductController {
    private final ProductServiceImpl productService;
    private final AuthServiceImpl authService;
    private final AccountServiceImpl accountService;
    private final ProductRepository productRepository;
    @PostMapping
    public ResponseEntity<Product> postProduct(
        @RequestHeader("Authorization") String accessToken,
        @RequestPart(value = "file") List<MultipartFile> multipartFile,
        @RequestPart(value="product") ProductRequest productRequest) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            Product product = productService.postProduct(spitToken[1],productRequest,
                multipartFile);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
        @Method: updateProduct
        @Author: frank
        @content: 상품 수정
        @return: Product
     */
    @PatchMapping("/{productId}")
    public ResponseEntity<Boolean> updateProduct(
        @RequestHeader("Authorization") String accessToken,
        @PathVariable Long productId,
        @RequestPart(value = "file") List<MultipartFile> multipartFile,
        @RequestPart(value="product") ProductUpdateRequest productUpdateRequest) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            Account account = accountService.findAccountByAccessToken(spitToken[1]);
            Boolean isUpdated =productService.updateProduct(productId,spitToken[1],multipartFile,productUpdateRequest);
            return new ResponseEntity<>(isUpdated, HttpStatus.CREATED);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public List<Product> listAllProducts() {
        log.info("Select All Products");
        List<Product> productList = productService.getAllProducts();
        return productList;
    }
    @GetMapping("/{productId}")
    public ResponseEntity<Product> viewPost(@PathVariable Long productId){
        log.info("{}",productId);
        Optional<Product> product=productRepository.findById(productId);
        product.get().setProductViewCnt(product.get().getProductViewCnt()+1);
        productRepository.save(product.get());
        return new ResponseEntity<>(product.get(),HttpStatus.OK);
    }
}
