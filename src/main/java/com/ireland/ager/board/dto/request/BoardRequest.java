package com.ireland.ager.board.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.entity.BoardUrl;
import lombok.Data;

import java.util.List;

@Data
public class BoardRequest {

    String title;
    String content;

    public static Board toBoard(BoardRequest boardRequest, Account account, List<String> uploadImgUrl) {
        Board board = new Board();

        for (String str : uploadImgUrl) {
            BoardUrl url = new BoardUrl();
            url.setUrl(str);
            board.addUrl(url);
        }
        board.addAccount(account);
        board.setTitle(boardRequest.getTitle());
        board.setContent(boardRequest.getContent());
        board.setBoardViewCnt(0L);
        return board;
    }

    public Board toBoardUpdate(Board board,
                               List<String> uploadImageUrl) {
        for (String str : uploadImageUrl) {
            BoardUrl url = new BoardUrl();
            url.setUrl(str);
            board.addUrl(url);
        }
        board.setTitle(this.title);
        board.setContent(this.content);
        return board;
    }
}
