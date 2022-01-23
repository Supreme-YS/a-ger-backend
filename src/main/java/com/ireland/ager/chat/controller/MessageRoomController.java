package com.ireland.ager.chat.controller;

import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.repository.MessageRoomRepository;
import com.ireland.ager.chat.service.MessageService;
import com.ireland.ager.config.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class MessageRoomController {
    private final MessageService messageService;
    private final MessageRoomRepository messageRoomRepository;
    /* 방 입장시 지금까지 나눈 정보가 필요하기 때문에 MessageRoom 정보가 필요하다.
     */
    @GetMapping("/{roomId}")
    public MessageRoom roomEnter(@PathVariable Long roomId) {
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);

        return messageRoom;
    }

    @PostMapping
    public MessageRoom insertRoom(
            @RequestParam Long productId,
            @RequestParam Long buyerId
    ) {
        return messageService.insertRoom(productId,buyerId);
    }
}
