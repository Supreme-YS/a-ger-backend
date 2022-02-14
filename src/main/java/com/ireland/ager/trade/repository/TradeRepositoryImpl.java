package com.ireland.ager.trade.repository;


import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.trade.entity.Trade;
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

import static com.ireland.ager.trade.entity.QTrade.trade;

@Repository
@RequiredArgsConstructor
public class TradeRepositoryImpl implements TradeRepositoryCustom  {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ProductThumbResponse> findBuyProductsByAccountId(Long accountId, Pageable pageable) {
        JPAQuery<Trade> tradeQuery = queryFactory
                .selectFrom(trade)
                .where(trade.buyerId.accountId.eq(accountId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1); //limit보다 한 개 더 들고온다.
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(trade.getType(), trade.getMetadata());
            tradeQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
        List<Trade> tradeList = tradeQuery.fetch();
        List<Product> productList=new ArrayList<>();
        for(Trade trade:tradeList) productList.add(trade.getProduct());
        List<ProductThumbResponse> content= new ArrayList<>(ProductThumbResponse.toProductListResponse(productList));
        boolean hasNext = false;
        //마지막 페이지는 사이즈가 항상 작다.
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
