package com.ireland.ager.chat.dto.response;

import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class MessageDetailsResponse {
    Long roomId;
    Set<Message> messages;

    public static MessageDetailsResponse toMessageDetailsResponse(MessageRoom messageRoom) {
        return MessageDetailsResponse.builder()
                .roomId(messageRoom.getRoomId())
                .messages(messageRoom.getMessages())
                .build();
    }
}
//TODO 채팅방 내역 주르륵 보이게 해야함