package com.ireland.ager.trade.repository;

import com.ireland.ager.product.dto.response.ProductThumbResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TradeRepositoryCustom {
    Slice<ProductThumbResponse> findBuyProductsByAccountId(Long accountId, Pageable pageable);
}
