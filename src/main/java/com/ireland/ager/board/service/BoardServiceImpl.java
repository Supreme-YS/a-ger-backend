package com.ireland.ager.board.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.board.dto.request.BoardRequest;
import com.ireland.ager.board.dto.response.BoardResponse;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.repository.BoardRepository;
import com.ireland.ager.main.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl {
    private final BoardRepository boardRepository;
    private final AccountServiceImpl accountService;

    public BoardResponse createPost(String accessToken,
                                    BoardRequest boardRequest) throws IOException {
        Account account = accountService.findAccountByAccessToken(accessToken);
        Board newPost = boardRepository.save(BoardRequest.toBoard(boardRequest, account));
        return BoardResponse.toBoardResponse(newPost);
    }

    public BoardResponse updatePost(String accessToken, Long boardId, BoardRequest boardRequest) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        Account account = accountService.findAccountByAccessToken(accessToken);
        if (!(account.equals(board.getAccountId()))) {
            throw new UnAuthorizedAccessException();
        }
        Board updatedPost = boardRepository.save(BoardRequest.toBoardUpdate(boardRequest, board));
        return BoardResponse.toBoardResponse(updatedPost);
    }

    public void deletePost(String accessToken, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        Account account = accountService.findAccountByAccessToken(accessToken);
        if (!(account.equals(board.getAccountId()))) {
            throw new UnAuthorizedAccessException();
        }
        boardRepository.deleteById(board.getBoardId());

    }

    //FIXME : viewCnt 체크하기
    public BoardResponse findPostById(String accessToken, Long boardId) {
        Account account = accountService.findAccountByAccessToken(accessToken);
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        if (!(account.equals(board.getAccountId()))) {
            return BoardResponse.toOtherBoard(board);
        }
        board.addViewCnt(board);
        return BoardResponse.toBoardResponse(board);
    }
}
