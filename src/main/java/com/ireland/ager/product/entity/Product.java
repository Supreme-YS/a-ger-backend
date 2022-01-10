package com.ireland.ager.product.entity;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> urlList =new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
}
