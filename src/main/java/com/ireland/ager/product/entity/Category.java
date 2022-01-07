package com.ireland.ager.product.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Category {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @ManyToOne
    private Product product;
}
