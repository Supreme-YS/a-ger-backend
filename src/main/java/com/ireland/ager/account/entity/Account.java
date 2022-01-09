package com.ireland.ager.account.entity;

import com.ireland.ager.config.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.ireland.ager.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Account extends BaseEntity {
    @Id @GeneratedValue
    Long accountId;
    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;
    String accessToken;
    String refreshToken;

    @OneToMany(mappedBy = "account")
    private List<Product> products;
}