package com.ireland.ager.chat.controller;

import com.ireland.ager.chat.dto.request.MessageRequest;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.service.KafkaProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/kafka")
@RequiredArgsConstructor
public class MessageController {
    private final KafkaProductService kafkaProductService;

    //producer 부분
    @PostMapping(value = "/publish")
    public void sendMessage(@RequestBody MessageRequest message) {
        kafkaProductService.sendMessage(message);
    }

    //여기서 프론트엔드로 메시지를 전송합니다.
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        return message;
    }
}