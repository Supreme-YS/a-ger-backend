package com.ireland.ager.chat.dto.response;

import com.ireland.ager.chat.entity.MessageRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomCreateResponse {
    Long roomId;

    public static RoomCreateResponse toRoomCreateResponse(MessageRoom messageRoom) {
        return RoomCreateResponse.builder()
                .roomId(messageRoom.getRoomId())
                .build();
    }
}
