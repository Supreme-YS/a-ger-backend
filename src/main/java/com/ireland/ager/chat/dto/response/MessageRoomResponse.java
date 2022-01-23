package com.ireland.ager.chat.dto.response;

import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MessageRoomResponse {
    Long roomId;
    String message;

    /*
    public List<MessageRoomResponse> toMessageRoomList(MessageRoom messageRoom, Message message) {
        return MessageRoomResponse.builder()
                .roomId(messageRoom.getRoomId())
                .message(message.getMessage())
                .build();
    }
    */
}