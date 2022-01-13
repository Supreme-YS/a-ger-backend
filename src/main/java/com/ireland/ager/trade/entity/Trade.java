package com.ireland.ager.trade.entity;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {

    @Id
    @GeneratedValue
    private Long tradeId;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Product product;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "buyer_account_id")
    private Account buyer;

    @ManyToOne
    @JoinColumn(name = "seller_account_id")
    private Account seller;

    //FIXME : 구매자, 판매자 구분 필요
    public static Trade toTrade(Account seller, Account buyer, Product product) {
        return Trade.builder()
                .seller(seller)
                .buyer(buyer)
                .product(product)
                .build();
    }
}
