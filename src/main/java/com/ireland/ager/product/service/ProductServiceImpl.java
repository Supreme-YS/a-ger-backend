package com.ireland.ager.product.service;

import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts(Long productId) {
        return productRepository.findAllByProductId(productId);
    }
}
