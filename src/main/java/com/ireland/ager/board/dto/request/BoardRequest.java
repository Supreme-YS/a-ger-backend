package com.ireland.ager.board.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardRequest {

    String title;
    String content;

    public static Board toBoard(BoardRequest boardRequest, Account account) {
        Board board = new Board();
        board.addAccount(account);
        board.setTitle(boardRequest.getTitle());
        board.setContent(boardRequest.getContent());
        board.setBoardViewCnt(0L);
        return board;
    }

    public static Board toBoardUpdate(BoardRequest boardRequest, Board board) {
        board.setTitle(boardRequest.getTitle());
        board.setContent(boardRequest.getContent());
        return board;
    }
}
