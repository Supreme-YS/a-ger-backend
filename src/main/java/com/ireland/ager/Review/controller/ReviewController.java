package com.ireland.ager.Review.controller;


//import com.ireland.ager.Review.dto.ReviewRequest;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.product.dto.request.ProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    //productId를 기준으로 리뷰를 작성한다.
//    @PostMapping("/{productId}")
//    public ResponseEntity<CommonResult> addReview(
//            @PathVariable Long productId,
//            @RequestPart(value = "review") ReviewRequest reviewRequest) {
//
//        return new ResponseEntity<>(,HttpStatus.OK);
//    }


}