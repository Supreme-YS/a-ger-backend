package com.ireland.ager.account.service;

import com.ireland.ager.board.dto.response.BoardSummaryResponse;
import com.ireland.ager.board.repository.BoardRepository;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.repository.ProductRepository;
import com.ireland.ager.product.service.ProductServiceImpl;
import com.ireland.ager.review.dto.response.ReviewResponse;
import com.ireland.ager.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountInfoServiceImpl {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;
    public Slice<ProductThumbResponse> findSellsByAccountId(Long accountId, Pageable pageable) {
        return productRepository.findSellProductsByAccountId(accountId,pageable);
    }

    public Slice<ReviewResponse> findReviewsByAccountId(Long accountId, Pageable pageable) {
        return reviewRepository.findReviewsByAccountId(accountId,pageable);
    }

    public Slice<BoardSummaryResponse> findBoardsByAccountId(Long accountId, Pageable pageable) {
        return boardRepository.findBoardsByAccountId(accountId,pageable);
    }
}
