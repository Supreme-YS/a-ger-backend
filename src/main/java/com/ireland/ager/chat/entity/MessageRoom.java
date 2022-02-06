package com.ireland.ager.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class MessageRoom extends BaseEntity {
    @Id
    @GeneratedValue
    private Long roomId;

    //순서 보장을 위해 linkedHashSet
    @OneToMany(mappedBy = "messageRoom",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Message> messages= new LinkedHashSet<>();

    private String latestMessage; //insertMessage 최신화되는? 1. 메세지보낼때마다 저장 2. 조회 리스트 10개 -> 10개만큼 조회

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "seller_id")
    private Account sellerId;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "buyer_id")
    private Account buyerId;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus=ReviewStatus.NOTSALE;

    private int roomStatus=3;
    public void toCreateMessageRoom(Product product,Account buyerId) {
        this.setBuyerId(buyerId);
        this.setSellerId(product.getAccount());
        this.setProduct(product);
    }

    public void toAddMessage(MessageRoom messageRoom,Message message) {
        messageRoom.getMessages().add(message);
        message.setMessageRoom(messageRoom);
        messageRoom.setLatestMessage(message.getMessage());
    }
}
