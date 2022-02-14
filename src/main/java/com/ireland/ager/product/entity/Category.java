package com.ireland.ager.product.entity;

import lombok.Getter;

@Getter
public enum Category {
    ELECTRONIC("전자기기"),
    HOUSEHOLDS("생활용품"),
    CLOTHES("의류"),
    OTHERS("기타"),
    BUY("삽니다");

    private String description;

    Category(String description) {
        this.description = description;
    }
}
