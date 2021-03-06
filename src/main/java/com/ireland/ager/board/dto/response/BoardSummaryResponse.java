package com.ireland.ager.board.dto.response;

import com.ireland.ager.board.entity.Board;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardSummaryResponse {
    Long boardId;
    String accountName;
    String title;
    String content;
    Long boardViewCnt;
    Long countComment;
    LocalDateTime createAt;

    public static BoardSummaryResponse toBoardSummaryResponse(Board board) {
        return BoardSummaryResponse.builder()
                .boardId(board.getBoardId())
                .accountName(board.getAccountId().getUserName())
                .title(board.getTitle())
                .content(board.getContent())
                .boardViewCnt(board.getBoardViewCnt())
                .countComment(board.getTotalCommentCount())
                .createAt(board.getCreatedAt())
                .build();
    }
}
