package com.ireland.ager.review.controller;


import com.ireland.ager.main.common.ListResult;
import com.ireland.ager.main.common.SingleResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.review.dto.request.ReviewRequest;
import com.ireland.ager.review.dto.response.ReviewResponse;
import com.ireland.ager.review.service.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewServiceImpl reviewService;
    private final ResponseService responseService;

    @GetMapping("/list/{accountId}")
    public ResponseEntity<ListResult<ReviewResponse>> getReviewList(
            @PathVariable Long accountId) {
        return new ResponseEntity<>(responseService.getListResult(reviewService.findReviewList(accountId)), HttpStatus.CREATED);
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<SingleResult<ReviewResponse>> postReview(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long roomId,
            @RequestPart(value = "review") ReviewRequest reviewRequest) {
        String[] splitToken = accessToken.split(" ");
        return new ResponseEntity<>(responseService.getSingleResult
                (reviewService.postReview(roomId, reviewRequest, splitToken[1])), HttpStatus.CREATED);
    }
}