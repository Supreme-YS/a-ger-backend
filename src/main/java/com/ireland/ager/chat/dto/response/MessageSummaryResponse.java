package com.ireland.ager.chat.dto.response;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.entity.MessageRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageSummaryResponse {
    Long roomId;
    String accountNickname;
    String accountThumbnail;

    public static MessageSummaryResponse toMessageSummaryResponse(MessageRoom messageRoom, Account account) {
        return MessageSummaryResponse.builder()
                .roomId(messageRoom.getRoomId())
                .accountNickname(account.getProfileNickname())
                .accountThumbnail(account.getProfileImageUrl())
                .build();
    }
}