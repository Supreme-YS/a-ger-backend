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
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query(value = "select p.productViewCnt from Product as p where p.productId = :productId")
    Long findProductViewCnt(Long productId);
}
