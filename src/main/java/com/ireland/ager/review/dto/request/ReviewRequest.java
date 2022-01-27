package com.ireland.ager.review.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.review.entity.Review;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
public class ReviewRequest {
    String comment;
    int stars;

    public static Review toReview(ReviewRequest reviewRequest, Account account, Product product){
        Review review = new Review();
        review.addAccount(account);
        review.setComment(reviewRequest.comment);
        review.setStars(reviewRequest.stars);
        review.setBuyerNickname(account.getProfileNickname());
        review.setTitle(product.getProductName());
        return  review;
    }

}
