package com.ireland.ager.chat.dto;

import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class MessageDto {
    private String messageType;
    private Long senderId;
    private String message;

    public Message toMessage(MessageDto messageDto) {
        return Message.builder()
                .message(messageDto.getMessage())
                .senderId(messageDto.getSenderId())
                .messageType(MessageType.valueOf(messageType))
                .build();
    }
}
