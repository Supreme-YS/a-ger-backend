package com.ireland.ager.review.repository;

import com.ireland.ager.review.dto.response.ReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewRepositoryCustom {
    Slice<ReviewResponse> findReviewsByAccountId(Long accountId, Pageable pageable);
}
