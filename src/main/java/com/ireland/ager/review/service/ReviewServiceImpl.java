package com.ireland.ager.review.service;

import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.entity.ReviewStatus;
import com.ireland.ager.chat.repository.MessageRoomRepository;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.review.dto.request.ReviewRequest;
import com.ireland.ager.review.entity.Review;
import com.ireland.ager.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {
    private final MessageRoomRepository messageRoomRepository;
    private final ReviewRepository reviewRepository;
    public void postReview(Long roomId,ReviewRequest reviewRequest) {
        MessageRoom messageRoom=messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);
        if(messageRoom.getReviewStatus().equals(ReviewStatus.SALE)){
            Review review=ReviewRequest.toReview(reviewRequest,messageRoom.getSellerId(),messageRoom.getProduct());
            messageRoom.setReviewStatus(ReviewStatus.POST);
            messageRoomRepository.save(messageRoom);
            reviewRepository.save(review);
        }
        else throw

    }
}
