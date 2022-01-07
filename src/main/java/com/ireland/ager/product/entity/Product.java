package com.ireland.ager.product.entity;

import com.ireland.ager.config.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Product extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String productName;

    private String productPrice;

    private String productDetail;

    private String productViewCnt;

    @OneToMany(mappedBy = "product")
    private Set<Category> categories = new HashSet<>();

    private Set<Photo> photos = new HashSet<>();
    
}
