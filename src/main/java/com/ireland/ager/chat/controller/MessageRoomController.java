package com.ireland.ager.chat.controller;

import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.repository.MessageRoomRepository;
import com.ireland.ager.chat.service.MessageService;
import com.ireland.ager.config.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class MessageRoomController {
    private final MessageService messageService;
    private final MessageRoomRepository messageRoomRepository;
    private final AuthServiceImpl authService;
    /* 방 입장시 지금까지 나눈 정보가 필요하기 때문에 MessageRoom 정보가 필요하다.
     */
    @GetMapping("/{roomId}")
    public MessageRoom roomEnter(@PathVariable Long roomId) {
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);
        return messageRoom;
    }

    @PostMapping("/{productId}")
    public MessageRoom insertRoom(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String accessToken
    ) {
        authService.isValidToken(accessToken);
        String[] splitToken = accessToken.split(" ");
        return messageService.insertRoom(productId,splitToken[1]);
    }
    //TODO: 내 채팅방 조회 기능. list
    @GetMapping
    public List<MessageSummaryResponse> getRoomList(
            @RequestHeader("Authorization") String accessToken
    ) {
        //TODO 반환 리스트를 줄때 room_id와 가장 최근 채팅을 보여주면 된다.
        authService.isValidToken(accessToken);
        String[] splitToken = accessToken.split(" ");
        return messageService.findRoomByAccessToken(splitToken[1]);
    }
}
