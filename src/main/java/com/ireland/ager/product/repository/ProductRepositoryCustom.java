package com.ireland.ager.product.repository;

import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
    Product addViewCnt(Long productId);
    Slice<ProductThumbResponse> findAllProductPageableOrderByCreatedAtDesc(Category category,String keyword,Pageable pageable);
}
