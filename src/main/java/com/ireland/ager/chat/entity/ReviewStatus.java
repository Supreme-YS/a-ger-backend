package com.ireland.ager.chat.entity;

public enum ReviewStatus {
    //리뷰 1. 판매중 등록x 2. 3.리뷰 게시
    NOTSALE("NOTSALE"),
    SALE("SALE"),
    POST("POST");

    private final String description;

    ReviewStatus(String description) {
        this.description = description;
    }
}
