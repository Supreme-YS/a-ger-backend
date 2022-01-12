package com.ireland.ager.trade.entity;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.Status;
import lombok.Builder;
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

    @Enumerated
    private Status status = Status.판매중;

    @Builder
    public void changeReserve(Account account, Product product, Status status) {
        this.account = account;
        this.product = product;
        this.status = Status.예약중;

        this.account.setStatus(Status.예약중);
        this.product.setStatus(Status.예약중);
    }

    @Builder
    public void changeSelling(Account account, Product product) {
        this.account = account;
        this.product = product;
        this.status = Status.판매중;

        this.account.setStatus(Status.판매중);
        this.account.setStatus(Status.판매중);
    }

    public void changeSoldout(Account account, Product product) {
        this.account = account;
        this.product = product;
        this.status = Status.판매완료;

        this.account.setStatus(Status.판매완료);
        this.account.setStatus(Status.판매완료);
    }
}
