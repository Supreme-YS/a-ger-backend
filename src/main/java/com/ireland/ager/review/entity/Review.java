package com.ireland.ager.review.entity;


import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    private String comment;
    private String title;
    private String buyerNickname;
    private Long buyerId;
    private String sellerNickname;
    private int stars;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Account sellerId;

    public void addAccount(Account account) {
        this.sellerId = account;
    }
}
