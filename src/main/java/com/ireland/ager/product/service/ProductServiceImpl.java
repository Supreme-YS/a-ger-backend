package com.ireland.ager.product.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.entity.Url;
import com.ireland.ager.product.exception.*;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.account.exception.UnAuthorizedTokenException;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.request.ProductUpdateRequest;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.repository.ProductRepository;
import com.ireland.ager.product.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Value("${cloud.aws.s3.bucket.name}") // 프로퍼티에서 cloud.aws.s3.bucket에 대한 정보를 불러옴
    public String bucket;
    private final AmazonS3Client amazonS3Client;

    //Todo 전체 조회에서 NotFound에러는 반환을 안해줍니다 상품이 없는건 에러가 아니라고 생각해서 프론트에서 빈 리스트를 보면 추가적인 멘트를 남기는 작업을 하면 될거 같습니다.
    //TODO : "더 이상 등록된 제품이 없습니다." 문구 추가 필요 - FRONTEND

    public List<ProductThumbResponse> findProductAllByCreatedAtDesc(Long productId, Integer size) {
        //TODO 일단 productId값을 가져와서 1을 더해준다. Lessthan이므로 1을 더해준다. 하지만 굳이 이러지 말고 productId값을 9999999999L값을 주어준다면 쿼리도 안날라가고 처리하기는 쉬우나 간지가 안난다.
        if(productId==0) {
            //productId=99999999999L;
            productId = productRepository.countProductByProductId(productId)+1;
            log.info("프로덕트 아이디 값은?? {}",productId);
        }
        Pageable pageRequest = PageRequest.of(0, size);
        return ProductThumbResponse.toProductListResponse(productRepository.findProductsByProductIdLessThanOrderByCreatedAtDesc(productId, pageRequest).getContent());
    }
    //TODO 조회수순으로 조회
    public List<ProductThumbResponse> findProductAllByProductViewCntDesc(Long productId, Integer size) {
        if(productId==0) {
            productId = productRepository.countProductByProductId(productId)+1;
        }
        PageRequest pageRequest = PageRequest.of(0, size);
        return ProductThumbResponse.toProductListResponse(productRepository.findProductByProductIdIsLessThanOrderByProductViewCntDesc(productId,pageRequest).getContent());
    }
    //TODO 카테고리별로 조회
    public List<ProductThumbResponse> findProductAllByCategory(Long productId, Integer size, String category) {
        if(productId==0) {
            productId = productRepository.countProductByProductId(productId)+1;
        }
        Pageable pageRequest = PageRequest.of(0, size);
        List<Product> productContent = productRepository.findProductsByProductIdLessThanAndCategoryOrderByCreatedAtDesc(
                productId, pageRequest, Category.valueOf(category)).getContent();
        return ProductThumbResponse.toProductListResponse(productContent);
    }

    public ProductResponse createProduct(String accessToken,
                                         ProductRequest productRequest,
                                         List<MultipartFile> multipartFile) throws IOException {
        Account account = accountService.findAccountByAccessToken(accessToken);
        //첫번째 이미지를 썸네일로 만들어서 업로드 해준다.
        String thumbNailUrl= uploadService.makeThumbNail(multipartFile.get(0));
        List<String> uploadImagesUrl = uploadService.uploadImages(multipartFile);
        Product product = productRequest.toProduct(account, uploadImagesUrl,thumbNailUrl);
        productRepository.save(product);
        return ProductResponse.toProductResponse(product);
    }

    //FIXME 캐시 적용 하는 곳,,
    @Cacheable(value = "product")
    public Product findProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        product.addViewCnt(product);
        productRepository.save(product);
        return product;
    }

    public ProductResponse updateProductById(Long productId,
                                             String accessToken,
                                             List<MultipartFile> multipartFile,
                                             ProductUpdateRequest productUpdateRequest) throws IOException {
        // 원래 정보를 꺼내온다.
        Product productById = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        if (!(productById.getAccount().getAccountId().equals(accountService.findAccountByAccessToken(accessToken).getAccountId()))) {
            // 수정하고자 하는 사람과 현재 토큰 주인이 다르면 False
            throw new UnAuthorizedTokenException();
        }
        validateFileExists(multipartFile);
        List<Url> currentFileImageUrlList = productById.getUrlList();
        String currentFileThumbnailUrl = productById.getThumbNailUrl();
        uploadService.delete(currentFileImageUrlList, currentFileThumbnailUrl);
        //여기서부터 연관 관계 삭제 하고 추가하는 과정 거친다.
        // product에 orphanremoval = true로 줘서 url의 pk가 null이 되면 delete되게 설정했다.
        for(Iterator<Url> it = productById.getUrlList().iterator() ; it.hasNext() ; )
        {
            Url url = it.next();
            url.setProduct(null);
            it.remove();
        }
        MultipartFile firstImage = multipartFile.get(0);
        List<String> updateFileImageUrlList = new ArrayList<>();
        try {
            updateFileImageUrlList = uploadService.uploadImages(multipartFile);
            productById.setThumbNailUrl(uploadService.makeThumbNail(firstImage));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        //REMARK 악취가 난다..... 해결 완료
        Account accountById = accountService.findAccountById(productById.getAccount().getAccountId());
        Product toProductUpdate = productUpdateRequest.toProductUpdate(productById, accountById, updateFileImageUrlList);
        productRepository.save(toProductUpdate);
        return ProductResponse.toProductResponse(toProductUpdate);
    }

    //FIXME 상품 삭제시 S3저장소에 이미지도 삭제하기 해결 완료
    public void deleteProductById(Long productId, String accessToken) {
        Product productById = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        if (!(productById.getAccount().getAccountId().equals(accountService.findAccountByAccessToken(accessToken).getAccountId()))) {
            // 수정하고자 하는 사람과 현재 토큰 주인이 다르면 False
            throw new UnAuthorizedTokenException();
        }
        uploadService.delete(productById.getUrlList(),productById.getThumbNailUrl());
        productRepository.deleteById(productId);
    }

    //Todo 들어온 파일리스트가 널값이면 사진갯수 에러를 반환하는 메서드이다. 하지만 파일의 갯수가 없어도 사이즈가 1로 찍힌다.
    // 파일 사이즈는 콘솔창에 업로드 파일의 갯수 찾아서 보면 확인 가능
    public void validateFileExists(List<MultipartFile> file) {
        if (file.isEmpty())
            throw new InvaildUploadException();
    }
    //Todo 업로드시에 입렵 폼 값 검증
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