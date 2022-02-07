package com.ireland.ager.board.repository;

import com.ireland.ager.board.dto.response.BoardResponse;
import com.ireland.ager.product.dto.response.ProductThumbResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardRepositoryCustom {
    Slice<BoardResponse> findAllBoardPageableOrderByCreatedAtDesc(String keyword, Pageable pageable);
}
