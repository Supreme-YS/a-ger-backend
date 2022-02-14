package com.ireland.ager.chat.entity;

public enum ReviewStatus {
    NOTSALE("NOTSALE"),
    SALE("SALE"),
    POST("POST");

    private final String description;

    ReviewStatus(String description) {
        this.description = description;
    }
}
