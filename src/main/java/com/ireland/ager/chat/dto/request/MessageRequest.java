package com.ireland.ager.chat.dto.request;

import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
//HINT 아래 두개를 추가해주니 오류가 안난다.
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest implements Serializable {
    private Long roomId;
    private Long senderId;
    private String message;

    public Message toMessage(MessageRequest messageDto, MessageRoom messageRoom) {
        return Message.builder()
                .message(messageDto.getMessage())
                .senderId(messageDto.getSenderId())
                .messageRoom(messageRoom)
                .build();
    }
}
