package com.ireland.ager.product.repository;

import com.ireland.ager.product.entity.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public void addViewCnt(Long productId) {
        QProduct product=QProduct.product;
        queryFactory
                .update(product)
                .set(product.productViewCnt,product.productViewCnt.add(1))
                .where(product.productId.eq(productId))
                .execute();
    }
}
