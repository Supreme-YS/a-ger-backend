package com.ireland.ager.chat.dto.request;

import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest implements Serializable {
    private Long roomId;
    private Long senderId;
    private String message;

    public static Message toMessage(MessageRequest messageDto, MessageRoom messageRoom) {
        return Message.builder()
                .message(messageDto.getMessage())
                .senderId(messageDto.getSenderId())
                .messageRoom(messageRoom)
                .build();
    }
}
