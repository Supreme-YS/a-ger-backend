package com.ireland.ager.account.entity;

import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
<<<<<<< HEAD
=======

>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
<<<<<<< HEAD
=======

>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Account extends BaseEntity {
    @Id
    @GeneratedValue
    Long accountId;
    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;
    String accessToken;
    String refreshToken;

<<<<<<< HEAD
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "account")
    List<Product> productList=new ArrayList<>();

=======
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    List<Product> productList = new ArrayList<>();
>>>>>>> bc7086266a1b5534629f215f7321bdc56ff2af40
}