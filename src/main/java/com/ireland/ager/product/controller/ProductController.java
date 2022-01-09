package com.ireland.ager.product.controller;

import com.ireland.ager.account.dto.response.AccountRes;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = {"*"}, maxAge = 6000)
@RequestMapping("/product")
public class ProductController {
    private final ProductServiceImpl productService;
    private final AuthServiceImpl authService;
    private final AccountServiceImpl accountService;

    @GetMapping("/")
    public List<Product> listAllProducts() {
        /**
         * @Method : listAllProducts
         * @Description : 등록된 모든 제품의 정보를 불러온다
         */
        log.info("Select All Products");
        List<Product> productList = productService.getAllProducts();
        return productList;
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductResponse> findProductById(
        /**
         * @Method : findProductById
         * @Description : 상품 하나의 정보를 불러온다
         */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable long productId) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            AccountRes userRes = accountService.findAccountByAccessToken(spitToken[1]);
            ProductResponse productResponse = productService.findProductById(productId);
            return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/post")
    public ResponseEntity<ProductResponse> postProduct(
            /**
             * @Method : postProduct
             * @Description : 판매자가 판매할 제품을 등록한다.
             */
            @RequestHeader("Authorization") String accessToken,
            @RequestPart MultipartFile productImage,
            @RequestPart ProductRequest productRequest) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            AccountRes userRes = accountService.findAccountByAccessToken(spitToken[1]);
//            ProductResponse productResponse = null;
            ProductResponse productResponse = productService.postProduct(productRequest, productImage);
            return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProductById (
        /**
         * @Method : updateProductById
         * @Description : 제품에 대한 정보를 수정한다.
         */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable long productId,
            @RequestPart(value = "productRequest") ProductRequest productRequest) {

        int vaildTokenStatusValue = authService.isValidToken(accessToken);
        if(vaildTokenStatusValue == 200) {
            String[] spitToken = accessToken.split(" ");
            AccountRes userRes = accountService.findAccountByAccessToken(spitToken[1]);
            Boolean isSuccess = productService.updateProductById(productId, productRequest, userRes.getAccessToken());
            if (isSuccess)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        else if (vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProductById(
        /**
         * @Method : deleteProdcutById
         * @Description : 상품 아이디를 기준으로 삭제한다
         */
            @RequestHeader("Authorization") String accessToken,
            @PathVariable long productId) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);

        if(vaildTokenStatusValue == 200) {
            productService.deleteProductById(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if(vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
