package com.ireland.ager.trade.entity;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Trade {

    @Id @GeneratedValue
    private Long tradeId;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Product product;

    @OneToOne
    private Manner manner;

    @Enumerated
    private Status status = Status.판매중;

    private Boolean tradeStatus;

    //FIXME : 구매자, 판매자 구분 필요
    @Builder
    public Trade(Boolean tradeStatus, Account account, Product product) {
        this.tradeStatus = tradeStatus;
        this.account = product.getAccount();
        this.product = product;
    }
}
