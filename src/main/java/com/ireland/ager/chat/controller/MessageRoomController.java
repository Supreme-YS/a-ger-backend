package com.ireland.ager.chat.controller;

import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.chat.dto.response.MessageDetailsResponse;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.chat.dto.response.RoomCreateResponse;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.service.MessageService;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.ListResult;
import com.ireland.ager.main.common.SingleResult;
import com.ireland.ager.main.common.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class MessageRoomController {
    private final MessageService messageService;
    private final AuthServiceImpl authService;
    private final ResponseService responseService;
    /* 방 입장시 지금까지 나눈 정보가 필요하기 때문에 MessageRoom 정보가 필요하다.
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<SingleResult<MessageDetailsResponse>> roomEnter(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String accessToken
    ) {
        authService.isValidToken(accessToken);
        String[] splitToken = accessToken.split(" ");
        //TODO buyer, seller인지 체크하는 로직 필요
        MessageDetailsResponse messageRoom = messageService.roomEnterByAccessToken(splitToken[1], roomId);
        return new ResponseEntity<>(responseService.getSingleResult(messageRoom), HttpStatus.OK);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<SingleResult<RoomCreateResponse>> insertRoom(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String accessToken
    ) {
        authService.isValidToken(accessToken);
        String[] splitToken = accessToken.split(" ");
        RoomCreateResponse roomCreateResponse = messageService.insertRoom(productId, splitToken[1]);
        return new ResponseEntity<>(responseService.getSingleResult(roomCreateResponse), HttpStatus.CREATED);
    }
    //TODO: 내 채팅방 조회 기능. list
    @GetMapping
    public ResponseEntity<ListResult<MessageSummaryResponse>> getRoomList(
            @RequestHeader("Authorization") String accessToken
    ) {
        //TODO 반환 리스트를 줄때 room_id와 가장 최근 채팅을 보여주면 된다.
        authService.isValidToken(accessToken);
        String[] splitToken = accessToken.split(" ");
        List<MessageSummaryResponse> roomByAccessToken = messageService.findRoomByAccessToken(splitToken[1]);
        return new ResponseEntity<>(responseService.getListResult(roomByAccessToken), HttpStatus.OK);
    }
    @DeleteMapping("/{roomId}")
    public ResponseEntity<CommonResult> roomDelete(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String accessToken
    ) {
        authService.isValidToken(accessToken);
        String[] splitToken = accessToken.split(" ");
        messageService.deleteById(splitToken[1],roomId);
        return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.OK);
    }
}
