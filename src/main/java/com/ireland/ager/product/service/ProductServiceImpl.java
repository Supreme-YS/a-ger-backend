package com.ireland.ager.product.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.exception.UnAuthorizedTokenException;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.main.service.UploadServiceImpl;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.request.ProductUpdateRequest;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.Url;
import com.ireland.ager.product.exception.*;
import com.ireland.ager.product.repository.ProductRepository;
import com.ireland.ager.product.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final UrlRepository urlRepository;
    private final AccountServiceImpl accountService;
    private final UploadServiceImpl uploadService;
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;
    @Value("${cloud.aws.s3.bucket.name}")
    public String bucket;
    private final AmazonS3Client amazonS3Client;
    private final RedisTemplate redisTemplate;

    public Slice<ProductThumbResponse> findProductAllByCreatedAtDesc(Category category, String keyword, Pageable pageable) {
        return productRepository.findAllProductPageableOrderByCreatedAtDesc(category,keyword,pageable);
    }

    public ProductResponse createProduct(String accessToken,
                                         ProductRequest productRequest,
                                         List<MultipartFile> multipartFile) throws IOException {
        Account account = accountService.findAccountByAccessToken(accessToken);
        String thumbNailUrl= uploadService.makeThumbNail(multipartFile.get(0));
        List<String> uploadImagesUrl = uploadService.uploadImages(multipartFile);
        Product product = productRequest.toProduct(account, uploadImagesUrl,thumbNailUrl);
        productRepository.save(product);
        return ProductResponse.toProductResponse(product,account);
    }

    @Cacheable(value = "product")
    public Product findProductById(Long productId) {
        return productRepository.addViewCnt(productId);
    }

    public void addViewCntToRedis(Long productId) {
        String key = "productViewCnt::"+productId;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if(valueOperations.get(key)==null)
            valueOperations.set(
                    key,
                    String.valueOf(productRepository.findProductViewCnt(productId)),
                    Duration.ofMinutes(5));
        else
            valueOperations.increment(key);
        log.info("value:{}",valueOperations.get(key));
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void deleteViewCntCacheFromRedis() {
        Set<String> redisKeys = redisTemplate.keys("productViewCnt*");
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String data = it.next();
            Long productId = Long.parseLong(data.split("::")[1]);
            Long viewCnt = Long.parseLong((String) redisTemplate.opsForValue().get(data));
            productRepository.addViewCntFromRedis(productId,viewCnt);
            redisTemplate.delete(data);
            redisTemplate.delete("product::"+productId);
        }
    }

    public ProductResponse updateProductById(Long productId,
                                             String accessToken,
                                             List<MultipartFile> multipartFile,
                                             ProductUpdateRequest productUpdateRequest) throws IOException {
        Product productById = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        if (!(productById.getAccount().getAccountId().equals(accountService.findAccountByAccessToken(accessToken).getAccountId()))) {
            throw new UnAuthorizedTokenException();
        }
        validateFileExists(multipartFile);
        List<Url> currentFileImageUrlList = productById.getUrlList();
        String currentFileThumbnailUrl = productById.getThumbNailUrl();
        uploadService.delete(currentFileImageUrlList, currentFileThumbnailUrl);
        productById.deleteUrl();
        MultipartFile firstImage = multipartFile.get(0);
        List<String> updateFileImageUrlList = new ArrayList<>();
        try {
            updateFileImageUrlList = uploadService.uploadImages(multipartFile);
            productById.setThumbNailUrl(uploadService.makeThumbNail(firstImage));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        Account accountById = accountService.findAccountById(productById.getAccount().getAccountId());
        Product toProductUpdate = productUpdateRequest.toProductUpdate(productById, accountById, updateFileImageUrlList);
        productRepository.save(toProductUpdate);
        return ProductResponse.toProductResponse(toProductUpdate,accountById);
    }

    public void deleteProductById(Long productId, String accessToken) {
        Product productById = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        if (!(productById.getAccount().getAccountId().equals(accountService.findAccountByAccessToken(accessToken).getAccountId()))) {
            throw new UnAuthorizedTokenException();
        }
        uploadService.delete(productById.getUrlList(),productById.getThumbNailUrl());
        productRepository.deleteById(productId);
    }

    public void validateFileExists(List<MultipartFile> file) {
        if (file.isEmpty())
            throw new InvaildUploadException();
    }
    public void validateUploadForm(BindingResult bindingResult){
        if(bindingResult.getErrorCount()>=3) throw new InvaildFormException();
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                String errorCode=objectError.getDefaultMessage();
                switch (Objects.requireNonNull(errorCode)){
                    case "3010" :  throw new InvaildProductTitleException();
                    case "3020" :
                    case "3021" :
                        throw new InvaildProductPriceException();
                    case "3030" :  throw new InvaildProductDetailException();
                    case "3040" :  throw new InvaildProductCategoryException();
                    case "3050" :  throw new InvaildProductStatusException();
                }
            });
        }
    }
}