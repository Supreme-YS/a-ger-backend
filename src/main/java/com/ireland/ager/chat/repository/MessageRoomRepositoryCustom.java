package com.ireland.ager.chat.repository;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageRoomRepositoryCustom {
    Slice<MessageSummaryResponse> findMessageRoomsBySellerIdOrBuyerId(Account sellerId, Account buyerId,Pageable pageable);
}
