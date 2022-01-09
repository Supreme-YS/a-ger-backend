package com.ireland.ager.product.entity;

import com.ireland.ager.config.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
@Getter
@Setter
public class Product extends BaseEntity {
    @Id @GeneratedValue
    private Long productId;

    private String productName;

    private String productPrice;

    private String productDetail;
    //조회수 설정
    @ColumnDefault("0")
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

}
