package com.ireland.ager.trade.entity;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Trade {

    @Id @GeneratedValue
    private Long tradeId;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Product product;

    @OneToOne
    private Manner manner;
}
