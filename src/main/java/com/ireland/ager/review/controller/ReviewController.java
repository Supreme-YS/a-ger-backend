package com.ireland.ager.review.controller;


//import com.ireland.ager.Review.dto.ReviewRequest;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.SingleResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.product.dto.request.ProductRequest;
import com.ireland.ager.product.dto.response.ProductResponse;
import com.ireland.ager.review.dto.request.ReviewRequest;
import com.ireland.ager.review.entity.Review;
import com.ireland.ager.review.service.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewServiceImpl reviewService;
    private final ResponseService responseService;
  @PostMapping("/{roomId}")
  public ResponseEntity<CommonResult> postReview(
          @RequestHeader("Authorization") String accessToken,
          @PathVariable Long roomId,
          @RequestPart(value = "review") ReviewRequest reviewRequest) {
      String[] splitToken = accessToken.split(" ");
      reviewService.postReview(roomId,reviewRequest,splitToken[1]);
      return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.OK);
  }



}