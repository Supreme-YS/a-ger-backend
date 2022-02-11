package com.ireland.ager.chat.entity;

public enum RoomStatus {
    EMPTY("EMPTY"),
    SELLEROUT("SELLEROUT"),
    BUYEROUT("BUYEROUT"),
    FULL("FULL");

    private final String description;

    RoomStatus(String description) {
        this.description = description;
    }
}
