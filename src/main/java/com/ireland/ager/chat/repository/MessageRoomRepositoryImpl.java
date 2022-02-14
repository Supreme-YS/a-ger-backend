package com.ireland.ager.chat.repository;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.entity.QMessage;
import com.ireland.ager.chat.entity.RoomStatus;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.ireland.ager.chat.entity.QMessageRoom.messageRoom;
import static com.ireland.ager.chat.entity.QMessage.message1;
@Repository
@RequiredArgsConstructor
public class MessageRoomRepositoryImpl implements MessageRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<MessageSummaryResponse> findMessageRoomsBySellerIdOrBuyerId(Account sellerId, Account buyerId, Pageable pageable) {
        List<MessageRoom> messageRoomList = queryFactory
                .selectFrom(messageRoom)
                .orderBy(messageRoom.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<MessageSummaryResponse> content = new ArrayList<>();
        for (MessageRoom messageRoom : messageRoomList) {
            Message message = queryFactory
                    .selectFrom(message1)
                    .where(message1.messageRoom.eq(messageRoom))
                    .orderBy(message1.createdAt.desc())
                    .fetchFirst();

            if (sellerId.equals(messageRoom.getSellerId())) {
                if (messageRoom.getRoomStatus().equals(RoomStatus.BUYEROUT)
                        || messageRoom.getRoomStatus().equals(RoomStatus.FULL)) {
                    content.add(MessageSummaryResponse.toMessageSummaryResponse(messageRoom, sellerId,message));
                }
            } else {
                if (messageRoom.getRoomStatus().equals(RoomStatus.SELLEROUT)
                        || messageRoom.getRoomStatus().equals(RoomStatus.FULL)) {
                    content.add(MessageSummaryResponse.toMessageSummaryResponse(messageRoom, buyerId,message));
                }
            }
        }
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
