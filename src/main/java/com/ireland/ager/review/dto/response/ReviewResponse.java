package com.ireland.ager.review.dto.response;


import com.ireland.ager.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ReviewResponse {
    Long buyerId;
    String buyerName;
    String comment;
    Long sellerId;
    String sellerNickname;
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

    public static List<ReviewResponse> toReviewResponse(List<Review> reviewList) {
        List<ReviewResponse> reviewResponseList = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewResponse reviewResponse = ReviewResponse.toReviewResponse(review);
            reviewResponseList.add(reviewResponse);
        }
        return reviewResponseList;
    }

}
