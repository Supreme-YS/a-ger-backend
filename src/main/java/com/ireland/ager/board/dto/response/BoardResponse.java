package com.ireland.ager.board.dto.response;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.entity.BoardUrl;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BoardResponse {
    Long boardId;
    String title;
    String content;
    Long boardViewCnt;
    List<BoardUrl> urlList;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    boolean isOwner;

    public static BoardResponse toBoardResponse(Board board, Account account) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .boardViewCnt(board.getBoardViewCnt())
                .urlList(board.getUrlList())
                .createAt(board.getCreatedAt())
                .updateAt(board.getUpdatedAt())
                .isOwner(board.getAccountId().equals(account))
                .build();
    }

    public static BoardResponse toOtherBoard(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .boardViewCnt(board.getBoardViewCnt())
                .urlList(board.getUrlList())
                .createAt(board.getCreatedAt())
                .updateAt(board.getUpdatedAt())
                .isOwner(false)
                .build();
    }

    public static List<BoardResponse> toBoardListResponse(List<Board> boardList) {
        List<BoardResponse> boardResponseList = new ArrayList<>();
        for (Board board : boardList) {
            BoardResponse boardResponse = BoardResponse.toBoardResponse(board, board.getAccountId());
            boardResponseList.add(boardResponse);
        }
        return boardResponseList;
    }
}
