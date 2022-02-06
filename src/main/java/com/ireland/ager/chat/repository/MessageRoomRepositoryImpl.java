package com.ireland.ager.chat.repository;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
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

@Repository
@RequiredArgsConstructor
public class MessageRoomRepositoryImpl implements MessageRoomRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<MessageSummaryResponse> findMessageRoomsBySellerIdOrBuyerId(Account sellerId, Account buyerId,Pageable pageable) {
        List<MessageRoom> messageRoomList= queryFactory
                .selectFrom(messageRoom)
                .orderBy(messageRoom.updatedAt.desc()) //가장 최근 대화순으로 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1) //limit보다 한 개 더 들고온다.
                .fetch();

        List<MessageSummaryResponse> content=new ArrayList<>();
        for(MessageRoom messageRoom:messageRoomList) {
            if(sellerId.equals(messageRoom.getSellerId())) { //seller일때
                if(messageRoom.getRoomStatus()==2||messageRoom.getRoomStatus()==3) {
                    content.add(MessageSummaryResponse.toMessageSummaryResponse(messageRoom,sellerId));
                }
            }
            else { //buyer일때
                if(messageRoom.getRoomStatus()==1||messageRoom.getRoomStatus()==3) {
                    content.add(MessageSummaryResponse.toMessageSummaryResponse(messageRoom,buyerId));
                }
            }
        }
        boolean hasNext =false;
        //마지막 페이지는 사이즈가 항상 작다.
        if(content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext=true;
        }
        return new SliceImpl<>(content,pageable,hasNext);
    }
}
