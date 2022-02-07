package com.ireland.ager.board.dto.response;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.board.entity.Board;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BoardResponse {
    Long boardId;
    Account accountId;
    String title;
    String content;
    Long boardViewCnt;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    boolean isOwner = true;

    public static BoardResponse toBoardResponse(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .accountId(board.getAccountId())
                .title(board.getTitle())
                .content(board.getContent())
                .boardViewCnt(board.getBoardViewCnt())
                .createAt(board.getCreatedAt())
                .updateAt(board.getUpdatedAt())
                .isOwner(true)
                .build();
    }

    public static BoardResponse toOtherBoard(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .accountId(board.getAccountId())
                .title(board.getTitle())
                .content(board.getContent())
                .boardViewCnt(board.getBoardViewCnt())
                .createAt(board.getCreatedAt())
                .updateAt(board.getUpdatedAt())
                .isOwner(false)
                .build();
    }

    public static List<BoardResponse> toBoardListResponse(List<Board> boardList) {
        List<BoardResponse> boardResponseList = new ArrayList<>();
        for (Board board : boardList) {
            BoardResponse boardResponse = BoardResponse.toBoardResponse(board);
            boardResponseList.add(boardResponse);
        }
        return boardResponseList;

    }
}
