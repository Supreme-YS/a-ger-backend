package com.ireland.ager.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.trade.entity.Trade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Product extends BaseEntity {
    @Id @GeneratedValue
    private Long productId;

    private String productName;

    private String productPrice;

    private String productDetail;
    private Long productViewCnt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status = Status.판매중;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> urlList =new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private Account account;

    //FIXME Optional Account 반환 문제 해결해야한다.
    public void addAccount(Account updateAccount) {
        updateAccount.getProductList().add(this);
        this.setAccount(updateAccount);
    }
    //FIXME : 거래 연관 관계 추가
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    private List<Trade> tradeList = new ArrayList<>();
}