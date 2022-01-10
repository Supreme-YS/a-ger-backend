package com.ireland.ager.product.controller;

import com.ireland.ager.account.dto.response.AccountRes;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import java.util.List;

import com.ireland.ager.product.service.UploadServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private  final UploadServiceImpl uploadService;

    /*
     *Product에 값을 집어 넣기
     */
    @PostMapping
    public ResponseEntity<Product> postProduct(
        @RequestHeader("Authorization") String accessToken,
        @RequestPart(value = "file") List<MultipartFile> multipartFile,
        @RequestPart(value="product") ProductRequest productRequest) {
        int vaildTokenStatusValue = authService.isValidToken(accessToken);
       // log.info("vaildTokenStatusValue  : ",vaildTokenStatusValue);
        if(vaildTokenStatusValue == 200) {
            Product product = productService.postProduct(productRequest,
                multipartFile);
            //log.info("list_url사이즈: ",product.getPhotoUrlList().size());
                return new ResponseEntity<>(product, HttpStatus.CREATED);
        } else if(vaildTokenStatusValue == 401) {
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

    @GetMapping
    public List<Product> listAllProducts() {
        log.info("Select All Products");
        List<Product> productList = productService.getAllProducts();
        return productList;
    }
    /*@PostMapping("/upload")
    public ResponseEntity uploadFile(
            @RequestPart(value = "file") List<MultipartFile> multipartFile)throws  Exception {

        List<String> upload_url = uploadService.uploadImages(multipartFile);
        if (!upload_url.isEmpty()) {
            return new ResponseEntity<>(upload_url, HttpStatus.OK);
        }else return new ResponseEntity<>("input image Error",HttpStatus.NOT_MODIFIED);
    }*/
}
