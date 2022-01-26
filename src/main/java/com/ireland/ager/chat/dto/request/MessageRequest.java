package com.ireland.ager.chat.dto.request;

import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageRequest {
    private String messageType;
    private Long senderId;
    private String message;

    public Message toMessage(MessageRequest messageDto) {
        return Message.builder()
                .message(messageDto.getMessage())
                .senderId(messageDto.getSenderId())
                .messageType(MessageType.valueOf(messageType))
                .build();
    }
}
