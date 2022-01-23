package com.ireland.ager.chat.controller;

import com.ireland.ager.chat.dto.MessageDto;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageType;
import com.ireland.ager.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final MessageService messageService;

    @MessageMapping("/message/{roomId}")
    public Message message(
            @PathVariable Long roomId,
            MessageDto messageDto) {
        Message message=messageDto.toMessage(messageDto);
        if(MessageType.ENTER.equals(message.getMessageType())) {
            message.setMessage(message.getSenderId()+"이 입장했습니다.");
        }
        messageService.insertMessage(roomId,message);
        sendingOperations.convertAndSend("sub/comm/room"+message.getMessageRoom().getRoomId());
        return message;
    }
}
