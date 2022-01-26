package com.ireland.ager.chat.controller;

import com.ireland.ager.chat.dto.request.MessageRequest;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.entity.MessageType;
import com.ireland.ager.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final MessageService messageService;

    @MessageMapping("/message/{roomId}")
    @SendTo("/sub/comm/room/{roomId}")
    public Message message(
            @DestinationVariable Long roomId,
            MessageRequest messageDto) {
        Message message=messageDto.toMessage(messageDto);
        if(MessageType.ENTER.equals(message.getMessageType())) {
            message.setMessage(message.getSenderId()+"이 입장했습니다.");
        }
        MessageRoom messageRoom = messageService.insertMessage(roomId, message);
        return message;
    }
}
