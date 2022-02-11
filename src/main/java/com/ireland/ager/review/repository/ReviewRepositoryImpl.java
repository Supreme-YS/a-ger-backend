package com.ireland.ager.review.repository;

import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.review.dto.response.ReviewResponse;
import com.ireland.ager.review.entity.Review;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.ireland.ager.review.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Slice<ReviewResponse> findReviewsByAccountId(Long accountId, Pageable pageable) {
        JPAQuery<Review> reviewQuery = queryFactory
                .selectFrom(review)
                .where(review.sellerId.accountId.eq(accountId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1); //limit보다 한 개 더 들고온다.
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(review.getType(), review.getMetadata());
            reviewQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
        List<ReviewResponse> content = new ArrayList<>(ReviewResponse.toReviewResponse(reviewQuery.fetch()));
        boolean hasNext = false;
        //마지막 페이지는 사이즈가 항상 작다.
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
