package com.ireland.ager.product.repository;

import com.ireland.ager.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query(value = "select p.productViewCnt from Product as p where p.productId = :productId")
    Long findProductViewCnt(Long productId);
}
