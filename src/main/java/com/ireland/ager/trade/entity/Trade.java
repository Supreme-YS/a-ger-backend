package com.ireland.ager.trade.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "tradeId", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "buyer_id")
    private Account buyerId;

    public static Trade toTrade(Product product, Account buyerId) {
        return Trade.builder()
                .product(product)
                .buyerId(buyerId)
                .build();
    }
}
