package com.ireland.ager.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue
    private Long messageId;
    private MessageType messageType;
    private Long senderId;
    private String message;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "messageRoom_id")
    private MessageRoom messageRoom;
}
