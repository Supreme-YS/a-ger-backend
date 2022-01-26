package com.ireland.ager.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.config.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {
    @Id
    @GeneratedValue
    private Long messageId;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    private Long senderId;
    private String message;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "room_id")
    private MessageRoom messageRoom;
}
