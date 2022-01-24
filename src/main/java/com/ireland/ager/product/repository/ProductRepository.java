package com.ireland.ager.product.repository;

import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    //Todo 상품 아이디보다 작고 높은 조회수인 상품 조회(페이징)
    Page<Product> findProductByProductIdIsLessThanOrderByProductViewCntDesc(Long productId,Pageable pageRequest);

    //Todo 상품아이디보다 작고 카테고리인 상품 조회(페이징)
    Page<Product> findProductsByProductIdLessThanAndCategoryOrderByCreatedAtDesc(Long productId,Pageable pageRequest, Category category);
    //Todo 상품아이디보다 작은 전체상품 조회(페이징)
    @EntityGraph(attributePaths = {"account","urlList"})
    Page<Product> findProductsByProductIdLessThanOrderByCreatedAtDesc(Long productId, Pageable pageRequest);
}
