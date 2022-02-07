package com.ireland.ager.board.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.board.dto.request.CommentRequest;
import com.ireland.ager.board.dto.response.CommentResponse;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.entity.Comment;
import com.ireland.ager.board.repository.BoardRepository;
import com.ireland.ager.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final AccountServiceImpl accountService;

    public CommentResponse saveComment(String accessToken, Board board, CommentRequest commentRequest) {
        Account account = accountService.findAccountByAccessToken(accessToken);
        Comment newComment = commentRepository.save(CommentRequest.toComment(commentRequest, account));
        return CommentResponse.toCommentResponse(newComment);
    }
}
