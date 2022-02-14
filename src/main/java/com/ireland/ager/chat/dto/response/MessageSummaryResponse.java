package com.ireland.ager.chat.dto.response;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageSummaryResponse {
    Long roomId;
    String accountNickname;
    String accountThumbnail;
    String latestMessage;
    LocalDateTime latestAt;

    public static MessageSummaryResponse toMessageSummaryResponse(MessageRoom messageRoom
            , Account account, Message message) {
        return MessageSummaryResponse.builder()
                .roomId(messageRoom.getRoomId())
                .accountNickname(account.getProfileNickname())
                .accountThumbnail(account.getProfileImageUrl())
                .latestMessage(message.getMessage())
                .latestAt(message.getCreatedAt())
                .build();
    }
}