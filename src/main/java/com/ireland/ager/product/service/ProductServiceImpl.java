package com.ireland.ager.product.service;

import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {
    private final ProductRepository productRepository;
    private final UploadServiceImpl uploadService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product postProduct(ProductRequest productRequest, List<MultipartFile> multipartFile) {

        List<String> uploadImagesUrl=uploadService.uploadImages(multipartFile);
        Product product=productRequest.toProduct(uploadImagesUrl);
        //상품 저장
        productRepository.save(product);
        return product;
    }

}
