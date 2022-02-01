package com.ireland.ager.board.service;

import com.ireland.ager.board.dto.request.BoardRequest;
import com.ireland.ager.board.repository.BoardRepository;
import com.ireland.ager.review.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl {
    private final BoardRepository boardRepository;

    @Transactional
    public ReviewResponse postBoard(BoardRequest boardRequest, String accessToken) {

    }
}
