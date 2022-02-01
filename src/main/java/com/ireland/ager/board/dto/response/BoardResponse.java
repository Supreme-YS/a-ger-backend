package com.ireland.ager.board.dto.response;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.board.entity.Board;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardResponse {
    Account accountId;
    String title;
    String content;
    LocalDateTime createAt;

    public static BoardResponse toBoardResponse(Board board) {
        return BoardResponse.builder()
                .accountId(board.getAccountId())
                .title(board.getTitle())
                .content(board.getContent())
                .createAt(board.getCreatedAt())
                .build();
    }
}
