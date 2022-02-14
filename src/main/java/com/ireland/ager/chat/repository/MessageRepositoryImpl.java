package com.ireland.ager.chat.repository;

import com.ireland.ager.chat.dto.request.MessageRequest;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.ireland.ager.chat.entity.QMessage.message1;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Message> findMessagesByMessageRequestOrderByCreatedAtDesc(Long messageRoomId, Pageable pageable) {
        List<Message> messageList = queryFactory
                .selectFrom(message1)
                //HINT 같은 룸 아이디만 가지고 온다. 여기서 많은 조회가 발생할거 같다.
                .where(message1.messageRoom.roomId.eq(messageRoomId))
                .orderBy(message1.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        //마지막 페이지는 사이즈가 항상 작다.
        if (messageList.size() > pageable.getPageSize()) {
            messageList.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(messageList, pageable, hasNext);
    }
}
