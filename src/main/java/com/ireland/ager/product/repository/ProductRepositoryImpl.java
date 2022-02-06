package com.ireland.ager.product.repository;

import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.QProduct;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
//static으로 Q-type 생성
import java.util.ArrayList;
import java.util.List;

import static com.ireland.ager.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Product addViewCnt(Long productId) {
        queryFactory
                .update(product)
                .set(product.productViewCnt,product.productViewCnt.add(1))
                .where(product.productId.eq(productId))
                .execute();
        return queryFactory.selectFrom(product).where(product.productId.eq(productId)).fetchOne();
    }

    @Override
    public Slice<ProductThumbResponse> findAllProductPageableOrderByCreatedAtDesc(Category category,String keyword ,Pageable pageable) {
        JPAQuery<Product> productQuery= queryFactory
                .selectFrom(product)
                .where(keywordContains(keyword),categoryEq(category))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1); //limit보다 한 개 더 들고온다.
        for(Sort.Order o: pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(product.getType(),product.getMetadata());
            productQuery.orderBy(new OrderSpecifier(o.isAscending()? Order.ASC: Order.DESC,pathBuilder.get(o.getProperty())));
        }
        List<ProductThumbResponse> content=new ArrayList<>(ProductThumbResponse.toProductListResponse(productQuery.fetch()));
        boolean hasNext =false;
        //마지막 페이지는 사이즈가 항상 작다.
        if(content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext=true;
        }
        return new SliceImpl<>(content,pageable,hasNext);
    }
    private BooleanExpression categoryEq(Category category) {
        return ObjectUtils.isEmpty(category) ? null : product.category.eq(category);
    }
    private BooleanExpression keywordContains(String keyword) {
        return ObjectUtils.isEmpty(keyword) ? null : product.productName.contains(keyword);
    }
}
