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
    String buyerName;
    Long buyerId;
    String comment;
    String sellerNickname;
    Long sellerId;
    int stars;
    LocalDateTime createdAt;
    public static ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .buyerName(review.getBuyerNickname())
                .buyerId(review.getBuyerId())
                .comment(review.getComment())
                .sellerNickname(review.getSellerNickname())
                .sellerId(review.getSellerId().getAccountId())
                .stars(review.getStars())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
