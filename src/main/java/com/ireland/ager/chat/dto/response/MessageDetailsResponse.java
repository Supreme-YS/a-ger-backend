package com.ireland.ager.chat.dto.response;

import com.ireland.ager.chat.entity.MessageRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageDetailsResponse {
    Long roomId;
    String reviewStatus;

    public static MessageDetailsResponse toMessageDetailsResponse(MessageRoom messageRoom) {
        return MessageDetailsResponse.builder()
                .roomId(messageRoom.getRoomId())
                .reviewStatus(messageRoom.getReviewStatus().name())
                .build();
    }
}