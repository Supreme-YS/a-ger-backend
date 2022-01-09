package com.ireland.ager.product.service;

import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.response.ProductResponse;
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductResponse postProduct(ProductRequest productRequest, MultipartFile multipartFile) {
        ProductResponse productResponse=null;
        return productResponse;
    }

    public ProductResponse findProductById(long productId) {
        return ProductResponse.of(productRepository.findById(productId).get());
    }
}
