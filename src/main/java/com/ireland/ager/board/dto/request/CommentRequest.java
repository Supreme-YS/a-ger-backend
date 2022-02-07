package com.ireland.ager.board.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.entity.Comment;
import lombok.Data;

@Data
public class CommentRequest {

    String commentContent;

    public static Comment toComment(CommentRequest commentRequest, Board board, Account account) {
        Comment comment = new Comment();
        comment.addAccount(account);
        comment.setBoardId(board);
        comment.setCommentContent(commentRequest.getCommentContent());
        return comment;
    }
}
