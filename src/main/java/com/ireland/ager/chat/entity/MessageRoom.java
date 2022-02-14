package com.ireland.ager.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class MessageRoom extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

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

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus=RoomStatus.FULL;

    public void toCreateMessageRoom(Product product,Account buyerId) {
        this.setBuyerId(buyerId);
        this.setSellerId(product.getAccount());
        this.setProduct(product);
    }
    public void updateRoomStatus(Account account) {
        if(this.getRoomStatus().equals(RoomStatus.EMPTY)) return;
        if(this.getRoomStatus().equals(RoomStatus.FULL)) {
            this.setRoomStatus((this.getSellerId().equals(account)? RoomStatus.SELLEROUT:RoomStatus.BUYEROUT));
        }
        else if(this.getRoomStatus().equals(RoomStatus.SELLEROUT) && this.getBuyerId().equals(account)) {
            this.setRoomStatus(RoomStatus.EMPTY);
        }
        else if(this.getRoomStatus().equals(RoomStatus.BUYEROUT) && this.getSellerId().equals(account)) { //buyerout
            this.setRoomStatus(RoomStatus.EMPTY);
        }
    }
}
