package com.ireland.ager.product.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.request.ProductUpdateRequest;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;


import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final AccountServiceImpl accountService;
    private final UploadServiceImpl uploadService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(String accessToken,
                               ProductRequest productRequest,
                               List<MultipartFile> multipartFile)
    {
        Account account = accountService.findAccountByAccessToken(accessToken);
        List<String> uploadImagesUrl = uploadService.uploadImages(multipartFile);
        Product product = productRequest.toProduct(account, uploadImagesUrl);
        //상품 저장
        productRepository.save(product);
        return product;
    }
    //FIXME 캐시 적용 하는 곳,,
    //@Cacheable(key = "#productId",value = "product")
    public Product findProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        product.get().addViewCnt(product.get());
        productRepository.save(product.get());
        return product.orElse(null);
    }
    public Boolean updateProductById(Long productId,
        String accessToken,
        List<MultipartFile> multipartFile,
        ProductUpdateRequest productUpdateRequest) {
        // 원래 정보를 꺼내온다.
        Optional<Product> productById = productRepository.findById(productId);

        if (!productById.isPresent()) {
            // 정보가 없다면 False
            return Boolean.FALSE;
        }
        if (!(productById.orElse(null).getAccount().equals(accountService.findAccountByAccessToken(accessToken)))) {
            // 수정하고자 하는 사람과 현재 토큰 주인이 다르면 False
            return Boolean.FALSE;
        }
        if (multipartFile != null) {
            List<String> updateFileImageUrlList = null;
            List<String> currentFileImageUrlList = productById.orElse(null).getUrlList();
            uploadService.delete(currentFileImageUrlList);
            try {
                updateFileImageUrlList = uploadService.uploadImages(multipartFile);
                productById.get().setUrlList(updateFileImageUrlList);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        // 원래 정보에 바뀐 정보를 업데이트
        Account accountById = accountService.findAccountById(productById.orElse(null).getAccount().getAccountId());
        Product toProductUpdate=productUpdateRequest.toProductUpdate(productById.orElse(null),accountById, productById.get().getUrlList());
        productRepository.save(toProductUpdate);
        return Boolean.TRUE;
    }

    //FIXME 상품 삭제시 S3저장소에 이미지도 삭제하기 해결 완료
    public void deleteProductById(Long productId) {
        uploadService.delete(productRepository
            .findById(productId).orElse(null)
            .getUrlList());
        productRepository.deleteById(productId);
    }
}