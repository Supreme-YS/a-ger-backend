//package com.ireland.ager.Review.dto;
//
//import com.ireland.ager.Review.entity.Review;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Builder
//public class ReviewDto {
//
//    private Long id;
//
//    @NotBlank(message = "판매자의 닉네임은 필수 항목입니다.")
//    private String nickname;
//
//    @NotBlank(message = "구매자의 닉네임은 필수 항목입니다.")
//    private String buyer;
//
//    @NotBlank(message = "거래 후기 내용은 필수 항목입니다.")
//    private String content;
//
//    public Review toEntity(ReviewDto reviewDto){
//        return Review.builder()
//                .buyer(reviewDto.getBuyer())
//                .content(reviewDto.getContent())
//                .build();
//    }
//
//    public static ReviewDto toDto(Review review){
//        return ReviewDto.builder()
//                .id(review.getId())
//                .nickname(review.getMember().getNickname())
//                .buyer(review.getBuyer())
//                .content(review.getContent())
//                .build();
//    }
//
//}
