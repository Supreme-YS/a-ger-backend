package com.ireland.ager.product.entity;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

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

    /*
        @Method: setCategory
        @Author: frank
        @param: category
        @content: 양방향 연관 관계 매핑
     */
    public void setCategory(Category category) {
        this.category=category;
        category.getProductList().add(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
}
