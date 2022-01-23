package com.ireland.ager.chat.entity;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRoom {
    @Id
    @GeneratedValue
    private Long roomId;

    //순서 보장을 위해 linkedHashSet
    @OneToMany(mappedBy = "messageRoom",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Message> messages= new LinkedHashSet<>();

    @ManyToOne

    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "buyer_id")
    private Account buyerId;

    public MessageRoom toCreateMessageRoom(Product product,Account buyerId) {
        this.setBuyerId(buyerId);
        this.setProduct(product);
        return this;
    }

    public MessageRoom toAddMessage(MessageRoom messageRoom,Message message) {
        messageRoom.getMessages().add(message);
        return messageRoom;
    }
}
