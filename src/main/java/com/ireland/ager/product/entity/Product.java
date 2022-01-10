package com.ireland.ager.product.entity;

import com.ireland.ager.config.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Photo> photoUrlList =new ArrayList<>();

}
