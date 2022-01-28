package com.ireland.ager.review.dto.response;


import com.ireland.ager.account.dto.response.AccountResponse;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.review.dto.request.ReviewRequest;
import com.ireland.ager.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class ReviewResponse {
    String buyerId;
    String comment;
    int stars;
    LocalDateTime createdAt;
    public static ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .buyerId(review.getBuyerNickname())
                .comment(review.getComment())
                .stars(review.getStars())
                .createdAt(review.getCreatedAt())
                .build();
    }

}
