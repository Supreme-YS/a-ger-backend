package com.ireland.ager.board.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.board.entity.Board;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardRequest {
    String title;
    String contents;
    LocalDateTime updatedAt;
    LocalDateTime createdAt;

    public static Board toBoard(BoardRequest boardRequest, Account account) {
        Board board = new Board();
        board.builder()
                .title(boardRequest.title)
                .content(boardRequest.contents)
                .accountId(account)
                .build();
        return board;
    }
}
