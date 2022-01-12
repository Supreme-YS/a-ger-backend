package com.ireland.ager.account.entity;

import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.trade.entity.Trade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class Account extends BaseEntity {
    @Id
    @GeneratedValue
    private Long accountId;
    private String accountEmail;
    private String profileNickname;
    private String userName;
    private String profileImageUrl;
    private String accessToken;
    private String refreshToken;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    private List<Product> productList = new ArrayList<>();

    //FIXME : 거래 연관 관계 추가
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    private List<Trade> tradeList = new ArrayList<>();
}