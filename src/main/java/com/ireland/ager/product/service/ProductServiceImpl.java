package com.ireland.ager.product.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.repository.AccountRepository;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.request.ProductUpdateRequest;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl {
    private final ProductRepository productRepository;
    private final UploadServiceImpl uploadService;

    private final AccountRepository accountRepository;
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product postProduct(String accessToken,
                            ProductRequest productRequest,
                            List<MultipartFile> multipartFile)
    {
        Optional<Account> account=accountRepository.findAccountByAccessToken(accessToken);

        List<String> uploadImagesUrl=uploadService.uploadImages(multipartFile);
        Product product=productRequest.toProduct(account,uploadImagesUrl);
        //상품 저장
        productRepository.save(product);

        return product;
    }
    public Optional<Product> findProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public Boolean updateProduct(Long productId,
                                String accessToken,
                                List<MultipartFile> multipartFile,
                                ProductUpdateRequest productUpdateRequest)
    {
        Optional<Product> productById = productRepository.findById(productId);
        if(!productById.isPresent()) {
            return Boolean.FALSE;
        }
        // 제품의 토큰 정보와 수정하고자 하는 유저의 토큰 정보가 다르다면
        if(!(productById.get().getAccount().getAccessToken().equals(accessToken))) {
            return Boolean.FALSE;
        }
        Product product = productById.get();
        productRepository.save(product);
        return Boolean.TRUE;
    }

    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }
}
