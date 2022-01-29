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

    public static Review toReview(ReviewRequest reviewRequest, Account seller, Product product,Account buyer){
        Review review = new Review();
        review.addAccount(seller);
        review.setComment(reviewRequest.comment);
        review.setStars(reviewRequest.stars);
        review.setBuyerNickname(buyer.getProfileNickname());
        review.setBuyerId(buyer.getAccountId());
        review.setSellerNickname(seller.getProfileNickname());
        review.setTitle(product.getProductName());
        return  review;
    }

}
